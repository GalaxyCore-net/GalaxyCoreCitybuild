package net.galaxycore.citybuild.bb

class BasicDeserializer {
    companion object {
        private fun convertByteToHexadecimal(byteArray: ByteArray): String {
            var hex = ""

            // Iterating through each byte in the array
            for (i in byteArray) {
                hex += String.format("%02X", i)
            }
            return hex
        }

        @JvmStatic
        fun disect(any: Any): String {
            if (any is ByteArray) {
                return convertByteToHexadecimal(any)
            }

            return any.toString()
        }
    }
}