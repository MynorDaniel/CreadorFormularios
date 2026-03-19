package com.example.creadorform.dominio

class Color(colorHex: String = "#000000") {

    private var _colorHex: String = colorHex

    val colorHex: String get() = _colorHex

    fun setHex(valor: String) {
        _colorHex = normalizarHex(valor)
    }

    fun setRgb(x: String, y: String, z: String) {
        val r = x.toIntOrNull() ?: 0
        val g = y.toIntOrNull() ?: 0
        val b = z.toIntOrNull() ?: 0
        _colorHex = rgbToHex(r, g, b)
    }

    fun setHsl(x: String, y: String, z: String) {
        val h = x.toFloatOrNull() ?: 0f
        val s = y.toFloatOrNull() ?: 0f
        val l = z.toFloatOrNull() ?: 0f
        val (r, g, b) = hslToRgb(h, s, l)
        _colorHex = rgbToHex(r, g, b)
    }

    private fun normalizarHex(valor: String): String {
        val v = valor.trim()
        return if (v.startsWith("#")) v else "#$v"
    }

    private fun rgbToHex(r: Int, g: Int, b: Int): String {
        fun clamp(v: Int) = v.coerceIn(0, 255)
        return "#%02X%02X%02X".format(clamp(r), clamp(g), clamp(b))
    }

    private fun hslToRgb(h: Float, sInput: Float, lInput: Float): Triple<Int, Int, Int> {
        val hNorm = ((h % 360f) + 360f) % 360f
        val s = if (sInput > 1f) (sInput / 100f) else sInput
        val l = if (lInput > 1f) (lInput / 100f) else lInput

        val c = (1f - kotlin.math.abs(2f * l - 1f)) * s
        val hPrime = hNorm / 60f
        val x = c * (1f - kotlin.math.abs(hPrime % 2f - 1f))

        val (r1, g1, b1) = when {
            hPrime < 1f -> Triple(c, x, 0f)
            hPrime < 2f -> Triple(x, c, 0f)
            hPrime < 3f -> Triple(0f, c, x)
            hPrime < 4f -> Triple(0f, x, c)
            hPrime < 5f -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        val m = l - c / 2f
        val r = ((r1 + m) * 255f).toInt().coerceIn(0, 255)
        val g = ((g1 + m) * 255f).toInt().coerceIn(0, 255)
        val b = ((b1 + m) * 255f).toInt().coerceIn(0, 255)
        return Triple(r, g, b)
    }
}

