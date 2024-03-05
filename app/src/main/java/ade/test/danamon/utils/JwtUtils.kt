package ade.test.danamon.utils

import io.jsonwebtoken.Jwts
import javax.crypto.SecretKey

object JwsUtils {
    private val key: SecretKey = Jwts.SIG.HS256.key().build()
    private const val SUBJECT = "DANAMON"

    fun generateJws(): String {
        return Jwts.builder().subject(SUBJECT).signWith(key).compact()
    }

    fun verifyJws(token: String): Boolean {
        return Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token).payload.subject.equals(SUBJECT)
    }
}