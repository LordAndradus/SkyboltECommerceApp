package edu.utsa.cs3443.skyboltecommerceapp.Helper

import android.util.Base64
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.keyPartTwo
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.ProfileViewModel.Companion.keyPartThree
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AES256Encryptor
{
    //This key is split into 3 parts. So, good luck going hunting for the whole key in memory!
    val keyPartOne: String = "SSBsb3Zl"

    /**
     * Assembles the three parts of the key to create a SecretKeySpec for use with AES256
     *
     * This further obfuscates the key because the attacker will have to search even more
     * for the key, as it will make the memory travel to here
     *
     * @return An encryption key
     */
    fun assembleKey(): SecretKey
    {
        val key = (keyPartOne + keyPartTwo + keyPartThree).toByteArray()
        return SecretKeySpec(key, "AES")
    }

    // Encrypt a string using AES-256
    fun encrypt(data: String): Pair<String, String>
    {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) } // Generate IV
        val ivSpec: AlgorithmParameterSpec = IvParameterSpec(iv)

        cipher.init(Cipher.ENCRYPT_MODE, assembleKey(), ivSpec)

        val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        val encryptedBase64 = Base64.encodeToString(encryptedData, Base64.DEFAULT)
        val ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT)

        // Return encrypted data and IV (Base64 encoded)
        return encryptedBase64 to ivBase64
    }

    // Decrypt an AES-256 encrypted string
    fun decrypt(encryptedData: String, iv: String): String
    {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

        cipher.init(Cipher.DECRYPT_MODE, assembleKey(), ivSpec)

        val decodedData = Base64.decode(encryptedData, Base64.DEFAULT)
        val originalData = cipher.doFinal(decodedData)

        return String(originalData, Charsets.UTF_8)
    }

    // Utility to convert a byte array to SecretKey
    fun byteArrayToSecretKey(key: ByteArray): SecretKey {
        return SecretKeySpec(key, 0, key.size, "AES")
    }
}