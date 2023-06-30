package dong.duan.photoedittor.edit_library.effect

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.media.effect.EffectContext
import android.media.effect.Effect
import android.media.effect.EffectFactory


class EffectFactory(private var bitmap: Bitmap) {

    fun apply_auto_fix(intensity: Float): Bitmap {
        val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)

        val canvas = Canvas(outputBitmap)
        val paint = Paint()

        val contrast = intensity * 2
        val brightness = intensity * 255 - 255

        val cm = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, brightness,
                0f, contrast, 0f, 0f, brightness,
                0f, 0f, contrast, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return outputBitmap
    }

    fun apply_auto_fix(contrast: Float, brightness: Float): Bitmap {
        val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)

        val canvas = Canvas(outputBitmap)
        val paint = Paint()

        val cm = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, brightness,
                0f, contrast, 0f, 0f, brightness,
                0f, 0f, contrast, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return outputBitmap
    }

    fun adjust_brightness(brightness: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(adjustedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()

        // Adjust the brightness using the ColorMatrix
        colorMatrix.set(
            floatArrayOf(
                1f, 0f, 0f, 0f, brightness.toFloat(),
                0f, 1f, 0f, 0f, brightness.toFloat(),
                0f, 0f, 1f, 0f, brightness.toFloat(),
                0f, 0f, 0f, 1f, 0f
            )
        )   // Apply the ColorMatrixColorFilter to the Paint
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return adjustedBitmap
    }

    fun adjust_contrast(contrast: Float): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(adjustedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()

        // Contrast adjustment
        val scale = contrast + 1f
        val translate = (-.5f * scale + .5f) * 255f
        colorMatrix.set(
            floatArrayOf(
                scale,
                0f,
                0f,
                0f,
                translate,
                0f,
                scale,
                0f,
                0f,
                translate,
                0f,
                0f,
                scale,
                0f,
                translate,
                0f,
                0f,
                0f,
                1f,
                0f
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return adjustedBitmap
    }

    fun adjust_saturation(saturation: Float): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(adjustedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()

        // Saturation adjustment
        colorMatrix.setSaturation(saturation)

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return adjustedBitmap
    }

    fun adjust_exposure(exposure: Float): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(adjustedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()

        // Exposure adjustment
        colorMatrix.set(
            floatArrayOf(
                exposure,
                0f,
                0f,
                0f,
                0f,
                0f,
                exposure,
                0f,
                0f,
                0f,
                0f,
                0f,
                exposure,
                0f,
                0f,
                0f,
                0f,
                0f,
                1f,
                0f
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return adjustedBitmap
    }

    fun adjust_tint(tint: Float): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(
            width, height, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(adjustedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()

        // Tint adjustment
        val r = 0xFF and ((tint * 255).toInt())
        val g = 0xFF and ((tint * 255).toInt())
        val b = 0xFF and ((tint * 255).toInt())
        colorMatrix.set(
            floatArrayOf(
                1f,
                0f,
                0f,
                0f,
                r.toFloat(),
                0f,
                1f,
                0f,
                0f,
                g.toFloat(),
                0f,
                0f,
                1f,
                0f,
                b.toFloat(),
                0f,
                0f,
                0f,
                1f,
                0f
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return adjustedBitmap
    }


    fun adjust_temperature(temperature: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(adjustedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()

        // Temperature adjustment
        val temp = (temperature * 2).toFloat() // Scaling the temperatureValue
        colorMatrix.set(
            floatArrayOf(
                1f, 0f, 0f, 0f, temp,
                0f, 1f, 0f, 0f, temp,
                0f, 0f, 1f, 0f, temp,
                0f, 0f, 0f, 1f, 0f
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return adjustedBitmap
    }


    fun adjust_tone(tone: Float): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(adjustedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()

        // Tone adjustment
        val toneValue = tone / 100f
        colorMatrix.set(
            floatArrayOf(
                toneValue,
                0f,
                0f,
                0f,
                0f,
                0f,
                toneValue,
                0f,
                0f,
                0f,
                0f,
                0f,
                toneValue,
                0f,
                0f,
                0f,
                0f,
                0f,
                1f,
                0f
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return adjustedBitmap
    }

    fun adjust_slossiness(glossiness: Float): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(adjustedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()

        // Glossiness adjustment
        val glossinessValue = glossiness / 100f
        colorMatrix.setScale(glossinessValue + 1f, glossinessValue + 1f, glossinessValue + 1f, 1f)
        colorMatrix.setSaturation(1f - glossinessValue)

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(
            bitmap,
            0f, 0f, paint
        )

        return adjustedBitmap
    }

    fun adjust_shadow(shadow: Float): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(adjustedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        val shadowValue = shadow / 100f
        colorMatrix.setScale(1f, 1f, 1f, 1f - shadowValue)

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return adjustedBitmap
    }

}