package dong.duan.photoedittor.edit_library.effect

import android.R.attr.src
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import dong.duan.photoedittor.file.Colors


class FilterFactory(private var bitmap: Bitmap) {
    fun apply_fresh(): Bitmap {
        val contrastValue = 1.2f // Adjust the contrast factor as desired
        val saturationValue = 1.2f // Adjust the saturation factor as desired

        val contrastMatrix = ColorMatrix()
        contrastMatrix.set(
            floatArrayOf(
                contrastValue, 0f, 0f, 0f, 0f,
                0f, contrastValue, 0f, 0f, 0f,
                0f, 0f, contrastValue, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )

        val saturationMatrix = ColorMatrix()
        saturationMatrix.setSaturation(saturationValue)

        val combinedMatrix = ColorMatrix()
        combinedMatrix.postConcat(contrastMatrix)
        combinedMatrix.postConcat(saturationMatrix)

        val filteredBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(filteredBitmap)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(combinedMatrix)
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return filteredBitmap
    }

    fun apply_transparent(): Bitmap {
        val transparencyPercentage = 50 // Adjust the transparency percentage as desired
        var alpha = (255 * (100 - transparencyPercentage) / 100).toInt()
        val filteredBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(filteredBitmap)
        val paint = Paint().apply {
            alpha = alpha
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return filteredBitmap
    }

    fun apply_warm(): Bitmap {
        val colorMatrix = ColorMatrix().apply {
            set(
                floatArrayOf(
                    1.2f, 0f, 0f, 0f, 0f, // Red channel
                    0f, 1f, 0f, 0f, 0f, // Green channel
                    0f, 0f, 0.8f, 0f, 0f, // Blue channel
                    0f, 0f, 0f, 1f, 0f // Alpha channel
                )
            )
        }

        // Apply the color matrix to the bitmap
        val filteredBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(filteredBitmap)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return filteredBitmap
    }


    fun apply_blackwhite(value: Float): Bitmap {
        val width: Int = bitmap.getWidth()
        val height: Int = bitmap.getHeight()
        // create output bitmap
        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, bitmap.getConfig())
        // color information
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int
        // get contrast value
        // get contrast value
        val contrast = Math.pow(((100 + value) / 100).toDouble(), 2.0)

        // scan through all pixels

        // scan through all pixels
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixel = bitmap.getPixel(x, y)
                A = Color.alpha(pixel)
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel)
                R = (((R / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (R < 0) {
                    R = 0
                } else if (R > 255) {
                    R = 255
                }
                G = Color.red(pixel)
                G = (((G / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (G < 0) {
                    G = 0
                } else if (G > 255) {
                    G = 255
                }
                B = Color.red(pixel)
                B = (((B / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (B < 0) {
                    B = 0
                } else if (B > 255) {
                    B = 255
                }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image

        // return final image
        return bmOut
    }


    fun apply_brightness(value: Int): Bitmap {
        val width: Int = bitmap.getWidth()
        val height: Int = bitmap.getHeight()

        val bmOut = Bitmap.createBitmap(width, height, bitmap.getConfig())
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixel = bitmap.getPixel(x, y)
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)

                // increase/decrease each channel
                R += value
                if (R > 255) {
                    R = 255
                } else if (R < 0) {
                    R = 0
                }
                G += value
                if (G > 255) {
                    G = 255
                } else if (G < 0) {
                    G = 0
                }
                B += value
                if (B > 255) {
                    B = 255
                } else if (B < 0) {
                    B = 0
                }

                // apply new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        return bmOut
    }

    fun apply_ice(): Bitmap {
        val convMatrix = ConvolutionMatrix(3)
        convMatrix.setAll(0.0)
        convMatrix.Matrix[0][0] = -2.0
        convMatrix.Matrix[1][1] = 2.0
        convMatrix.Factor = 1.0
        convMatrix.Offset = 95.0
        return ConvolutionMatrix.computeConvolution3x3(bitmap, convMatrix)
    }

    fun apply_shading(): Bitmap {
        val shadingColor= Colors.GRAY
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        // get pixel array from source
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        var index = 0
        // iteration through pixels
        for (y in 0 until height) {
            for (x in 0 until width) {
                // get current index in 2D-matrix
                index = y * width + x
                // AND
                pixels[index] = pixels[index] and shadingColor
            }
        }
        // output bitmap
        val bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height)
        return bmOut
    }

    fun apply_special(): Bitmap {
        val colorMatrix = ColorMatrix().apply {
            setSaturation(1.8f) // Increase saturation for vibrant colors
            set(
                floatArrayOf(
                    2f, 0f, 0f, 0f, 0f, // Red channel
                    0f, 2f, 0f, 0f, 0f, // Green channel
                    0f, 0f, 2f, 0f, 0f, // Blue channel
                    0f, 0f, 0f, 1f, 0f  // Alpha channel
                )
            )
        }
        val filteredBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(filteredBitmap)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return filteredBitmap
    }

    fun apply_classic(): Bitmap {
        // Create a color matrix for the classic filter
        val colorMatrix = ColorMatrix().apply {
            // Reduce saturation to create a faded look
            setSaturation(0.5f)

            // Apply a slight sepia tone
            set(
                floatArrayOf(
                    0.393f, 0.769f, 0.189f, 0f, 0f, // Red channel
                    0.349f, 0.686f, 0.168f, 0f, 0f, // Green channel
                    0.272f, 0.534f, 0.131f, 0f, 0f, // Blue channel
                    0f, 0f, 0f, 1f, 0f // Alpha channel
                )
            )

            // Adjust contrast for a vintage feel
            postConcat(ColorMatrix().apply {
                setScale(0.8f, 0.8f, 0.8f, 1f)
            })
        }
        val filteredBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(filteredBitmap)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return filteredBitmap
    }

    fun apply_fade(): Bitmap {
        val filteredBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixel = bitmap.getPixel(x, y)
                val alpha = Color.alpha(pixel)
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)

                val fadedPixel = Color.argb(alpha, red - 1, green - 1, blue - 1)
                filteredBitmap.setPixel(x, y, fadedPixel)
            }
        }
        return filteredBitmap
    }

    fun apply_gray(): Bitmap {
        val matrix = ColorMatrix().apply {
            setSaturation(0f) // Set saturation to 0 for grayscale
        }

        val filteredBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(filteredBitmap)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(matrix)
        }

        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return filteredBitmap
    }

    fun apply_blind(): Bitmap {
        val filteredBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(filteredBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix().apply {
            setSaturation(0f) // Desaturate the image
            postConcat(
                ColorMatrix(
                    floatArrayOf(
                        0.5f, 0.5f, 0.5f, 0f, 0f,
                        0.5f, 0.5f, 0.5f, 0f, 0f,
                        0.5f, 0.5f, 0.5f, 0f, 0f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
            )
        }
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return filteredBitmap
    }

    fun apply_orange(): Bitmap {
        val filteredBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(filteredBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix().apply {
            set(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 50f, // Add 50 to the red channel
                    0f, 0.8f, 0f, 0f, 0f, // Multiply the green channel by 0.8
                    0f, 0f, 0f, 0f, 0f, // Set the blue channel to 0
                    0f, 0f, 0f, 1f, 0f // Alpha channel (unchanged)
                )
            )
        }
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return filteredBitmap
    }

    fun apply_brilliantpink(): Bitmap {
        val filteredBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(filteredBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix().apply {
            setSaturation(1f) // Retain the original saturation
            set(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f, // Red channel
                    0.4f, 0f, 0.4f, 0f, 0f, // Green and blue channels
                    0.8f, 0f, 1f, 0f, 0f, // Mix of red and blue channels
                    0f, 0f, 0f, 1f, 0f // Alpha channel
                )
            )
        }
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return filteredBitmap
    }

    fun apply_neural(): Bitmap {
        // Apply ND filter
        val ndFilteredBitmap = applyNDFilter(bitmap, NDFilter(0.5f))

        // Apply Auto White Balance
        val awbFilteredBitmap = applyAutoWhiteBalance(ndFilteredBitmap, AutoWhiteBalance())

        return awbFilteredBitmap
    }


    private data class NDFilter(
        val contrast: Float = 1.0f
    )

    private data class AutoWhiteBalance(
        val targetNeutral: FloatArray = floatArrayOf(1f, 1f, 1f),
        val currentNeutral: FloatArray = floatArrayOf(0f, 0f, 0f)
    )

    private fun applyNDFilter(bitmap: Bitmap, filter: NDFilter): Bitmap {
        val filteredBitmap = bitmap.copy(bitmap.config, true)
        val contrast = filter.contrast

        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (i in pixels.indices) {
            val color = pixels[i]
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)

            val adjustedR = (r * contrast).toInt().coerceIn(0, 255)
            val adjustedG = (g * contrast).toInt().coerceIn(0, 255)
            val adjustedB = (b * contrast).toInt().coerceIn(0, 255)

            pixels[i] = Color.rgb(adjustedR, adjustedG, adjustedB)
        }

        filteredBitmap.setPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        return filteredBitmap
    }

    private fun applyAutoWhiteBalance(bitmap: Bitmap, awb: AutoWhiteBalance): Bitmap {
        val filteredBitmap = bitmap.copy(bitmap.config, true)
        val targetNeutral = awb.targetNeutral
        val currentNeutral = awb.currentNeutral

        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (i in pixels.indices) {
            val color = pixels[i]
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)

            val adjustedR = (r * targetNeutral[0] / currentNeutral[0]).toInt().coerceIn(0, 255)
            val adjustedG = (g * targetNeutral[1] / currentNeutral[1]).toInt().coerceIn(0, 255)
            val adjustedB = (b * targetNeutral[2] / currentNeutral[2]).toInt().coerceIn(0, 255)

            pixels[i] = Color.rgb(adjustedR, adjustedG, adjustedB)
        }

        filteredBitmap.setPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        return filteredBitmap
    }

}