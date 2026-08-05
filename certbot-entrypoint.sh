#!/bin/sh

set -eu

: "${LETSENCRYPT_EMAIL:?LETSENCRYPT_EMAIL nao configurado}"

certificate=/etc/letsencrypt/live/api.webdev.computer/fullchain.pem
private_key=/etc/letsencrypt/live/api.webdev.computer/privkey.pem

certificate_is_valid() {
    [ -f "$certificate" ] &&
        [ -f "$private_key" ] &&
        openssl x509 -checkend 86400 -noout -in "$certificate" >/dev/null 2>&1 &&
        [ "$(openssl x509 -pubkey -noout -in "$certificate" 2>/dev/null)" = \
          "$(openssl pkey -pubout -in "$private_key" 2>/dev/null)" ]
}

if ! certificate_is_valid; then
    set --
    if [ -e "$certificate" ] || [ -e "$private_key" ]; then
        set -- --cert-name api.webdev.computer --force-renewal
    fi

    sleep 10
    until certbot certonly \
        --webroot \
        --webroot-path /var/www/certbot \
        --domain api.webdev.computer \
        --email "$LETSENCRYPT_EMAIL" \
        --agree-tos \
        --no-eff-email \
        --non-interactive \
        "$@"; do
        echo 'Falha ao emitir o certificado; nova tentativa em 5 minutos'
        sleep 300
    done
fi

while :; do
    certbot renew \
        --webroot \
        --webroot-path /var/www/certbot \
        --non-interactive
    sleep 12h & wait $!
done
