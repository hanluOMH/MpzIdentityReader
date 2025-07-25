package org.multipaz.identityreader.backend

import kotlinx.io.bytestring.ByteString
import org.multipaz.rpc.backend.Configuration
import org.multipaz.util.Logger
import org.multipaz.util.fromBase64Url

class Settings(private val conf: Configuration) {

    val readerCertValidityDays
        get() = getInt("reader_cert_validity_days", 30)

    val iosReleaseBuild
        get() = getBool("ios_require_release_build", false)

    val iosAppIdentifier
        get() = getString("ios_require_app_identifier")

    val androidRequireGmsAttestation
        get() = getBool("android_require_gms_attestation", true)

    val androidRequireVerifiedBootGreen
        get() = getBool("android_require_verified_boot_green", true)

    val androidRequireAppSignatureCertificateDigests: List<ByteString>
        get() = getStringList("android_require_app_signature_certificate_digests").map {
            ByteString(it.fromBase64Url())
        }

    private fun getString(key: String) = conf.getValue(key)

    private fun getBool(key: String, defaultValue: Boolean = false): Boolean {
        val value = conf.getValue(key)
        if (value == null) {
            Logger.d(TAG, "getBool: No value configuration value with key $key, return default value $defaultValue")
            return defaultValue
        }
        if (value == "true") {
            return true
        } else if (value == "false") {
            return false
        }
        Logger.d(TAG, "getBool: Unexpected value '$value' with key $key, return default value $defaultValue")
        return defaultValue
    }

    private fun getInt(key: String, defaultValue: Int = 0): Int {
        val value = conf.getValue(key)
        if (value == null) {
            Logger.d(TAG, "getInt: No value configuration value with key $key, return default value $defaultValue")
            return defaultValue
        }
        try {
            return value.toInt()
        } catch (e: Throwable) {
            Logger.d(TAG, "getInt: Unexpected value '$value' with key $key, return default value $defaultValue")
        }
        return defaultValue
    }

    fun getStringList(key: String): List<String> {
        val value = conf.getValue(key)
        if (value == null) {
            Logger.d(TAG, "getStringList: No value configuration value with key $key")
            return emptyList()
        }
        if (value.isEmpty()) {
            return emptyList()
        }
        return value.split("\\s+".toRegex())
    }

    companion object {
        private const val TAG = "Settings"
    }
}
