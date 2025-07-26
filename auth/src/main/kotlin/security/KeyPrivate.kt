package org.example.security

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

private val privateKey = """
    -----BEGIN PRIVATE KEY-----
MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCxgJL5XnzpegRW
1diAM8w8mFccal6GUtXXPVxILUijgdA4o4DA60csXxuo9r9RRPJx57Pw0V9CR+1f
Xu+7SXLhnhmM3CjMlvmB9HTGSZPyiPSFFLJ1ufjGeX9ZV0YnhlxBYi0/LC9yfyU0
9AksCxdK4gd8MLHI6obdI1KqxWFmF3/H3ypJKK/aUjyM2+NloZtTFKAb2D32M//P
IO99T0fSxZlu1Mll5j908YnCGGjF1DOY8NjeOTPWmqhh5pk6DzkD/Ij+Uc2BhFy4
NbAjpRYcXFo4LK5U+6VsP289aub85OAatQTlJvN5WVIV0gfQAODRMszYi1WUptyS
qzJddEEBAgMBAAECggEANDmqxMFGymEytqPrs+jJnkiKVNt9HVNxBIfSx4+/UBBp
RcjhoKF5CvaeyktzRgOdu+LbAC9yMiu8jTN/IKE/Ml/Uj6vVO0Duf4x/8TmD1Lf0
DdXlnhrONgNYc15oz0dKjJhIzM4A1dTQCzEEI9fNjiRL5H/lR0Kt1JHIiGNTwXgI
t5NuSU6TOgf+NT/QSOVaO7m9/H+xCd+n3CIahms/iUlnHTFlsPsARtF53A4lTfLh
3XoLyWg23kSnMe4ymQskSIGygjVXGVMskZE7WfgXYxZDIB0O4EU+g8UG2iC29wg7
gM9YpweLIkaUvJaxT5A6CCAht05zfjhMW4vdthSukwKBgQD3Qk5W8RnCOav9XNtZ
wSiRzxdy3eZT/oyHu2puworjR5c746hNHOw9mrHuFrIazpLQi8JyVT5OXXyCxVbz
QIo2se+WQnFQZQsQV6auDStrRZQVdl8BclxesYZdWR821xEeVf78Bwr+PVp2vh4l
JDOetBCmafZTeHeglK48954QqwKBgQC3xvgp0pLfyRzT01puUt/60weYhkIwQgFk
k7Jg4taHXDr3Rm+GkAJBSf3a24vHWzd8O1nIr/Ip325h9TOhSPLLAL1+/GAHqQ2E
J3/ygWIhYW5kVhqO9nf30lPeqcXco9RtSMCcFVe0qTCtCFuej3Mk6iFgJRGs3FyN
SqLDQQQtAwKBgBCQCntcbyNJhbHVFIRwoe0SeDmjj5g0xukYrsp4kaEj3IeQemef
9lsyD5UpRAjzqXX6xq2t9Bx/uqVv3qEww5FiXfAsxzZhblpL5Mhn7W6kHIVLhpWs
OW/GaH+8RpD/2w+zJv9u4VeieHrXxWEriaGp4iujkXKJ8Ul6bCl9VMM9AoGARL9F
rj97egN9qS/zMDoIV4gB22i98dd/NeSaYev5XIF//Wh359qPPWU0dV6Tl2vkAbEi
ERTgfhF69WIIglkLmi0YthwqwDLZP4qXLlyAHQSZiyc6oChLEIW5oh3YSoVuvcLt
anYBbS+tac0qmNIIvNJo0dKMHiIA7UnyQSBdM4kCgYBUf4mibMj+5uvkoUYen2o0
YJMflAukGkzgrsX2xK2IVRqQAz/L+vOHM2lUkwit2nrq/XaKQMzcVTiX60iAZLW0
sW4xUy1vgKYO+EcnPg7cxd4d6QdQrAEDCG6GOL2X1YE8dIRDgGm7qD+56iglgvxY
LYwYqGJThcQuyyIw1xHzHw==
-----END PRIVATE KEY-----
""".trimIndent()

fun loadPrivateKey(): PrivateKey {
    val clean = privateKey.replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replace("\\s".toRegex(), "")

    val bytes = Base64.getDecoder().decode(clean)
    val spec = PKCS8EncodedKeySpec(bytes)
    return KeyFactory.getInstance("RSA").generatePrivate(spec)
}
