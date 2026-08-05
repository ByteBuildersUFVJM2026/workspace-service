#!/bin/sh

set -eu

bootstrap_config=/etc/nginx/configs/bootstrap.conf
https_config=/etc/nginx/configs/https.conf
active_config=/etc/nginx/nginx.conf
certificate=/etc/letsencrypt/live/api.webdev.computer/fullchain.pem
private_key=/etc/letsencrypt/live/api.webdev.computer/privkey.pem

activate_config() {
    cp "$1" "$active_config"
    nginx -t
}

https_config_is_valid() {
    [ -f "$certificate" ] &&
        [ -f "$private_key" ] &&
        nginx -t -c "$https_config" >/dev/null 2>&1
}

certificate_checksum() {
    cksum "$certificate" "$private_key" | cksum | awk '{print $1 ":" $2}'
}

if https_config_is_valid; then
    activate_config "$https_config"
    last_certificate_checksum="$(certificate_checksum)"
else
    activate_config "$bootstrap_config"
    last_certificate_checksum=''
fi

(
    while :; do
        sleep 10

        if https_config_is_valid; then
            current_certificate_checksum="$(certificate_checksum)"

            if ! cmp -s "$https_config" "$active_config"; then
                activate_config "$https_config"
                nginx -s reload
                last_certificate_checksum="$current_certificate_checksum"
            elif [ "$current_certificate_checksum" != "$last_certificate_checksum" ]; then
                nginx -s reload
                last_certificate_checksum="$current_certificate_checksum"
            fi
        elif ! cmp -s "$bootstrap_config" "$active_config"; then
            echo 'Certificado TLS invalido; retornando ao modo HTTP para recuperacao'
            activate_config "$bootstrap_config"
            nginx -s reload
            last_certificate_checksum=''
        fi
    done
) &

exec nginx -g 'daemon off;'
