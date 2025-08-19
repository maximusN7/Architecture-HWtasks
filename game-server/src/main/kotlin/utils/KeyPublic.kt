package org.example.utils

import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

private val publicKey = """
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsYCS+V586XoEVtXYgDPM
PJhXHGpehlLV1z1cSC1Io4HQOKOAwOtHLF8bqPa/UUTyceez8NFfQkftX17vu0ly
4Z4ZjNwozJb5gfR0xkmT8oj0hRSydbn4xnl/WVdGJ4ZcQWItPywvcn8lNPQJLAsX
SuIHfDCxyOqG3SNSqsVhZhd/x98qSSiv2lI8jNvjZaGbUxSgG9g99jP/zyDvfU9H
0sWZbtTJZeY/dPGJwhhoxdQzmPDY3jkz1pqoYeaZOg85A/yI/lHNgYRcuDWwI6UW
HFxaOCyuVPulbD9vPWrm/OTgGrUE5SbzeVlSFdIH0ADg0TLM2ItVlKbckqsyXXRB
AQIDAQAB
-----END PUBLIC KEY-----
""".trimIndent()

fun loadPublicKey(): PublicKey {
    val clean = publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace("\\s".toRegex(), "")

    val bytes = Base64.getDecoder().decode(clean)
    val spec = X509EncodedKeySpec(bytes)
    return KeyFactory.getInstance("RSA").generatePublic(spec)
}
