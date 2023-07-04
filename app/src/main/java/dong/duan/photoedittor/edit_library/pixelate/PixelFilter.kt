package dong.duan.photoedittor.edit_library.pixelate

import android.R.attr
import android.graphics.Bitmap


class PixelFilter(val bitmap:Bitmap) {
    val first_filter get() :Bitmap {
      return  Pixelate.fromBitmap(
            bitmap,
            PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                .setResolution(48f)
                .setSize(50f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                .setResolution(48f)
                .setOffset(24f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(8f)
                .setSize(6f)
                .build()
        )
    }
    val second_filter get() :Bitmap {
        return Pixelate.fromBitmap(bitmap,
            PixelateLayer.Builder(PixelateLayer.Shape.Square)
                .setResolution(32f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(32f)
                .setOffset(15f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(32f)
                .setSize(26f)
                .setOffset(13f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(32f)
                .setSize(18f)
                .setOffset(10f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(32f)
                .setSize(12f)
                .setOffset(8f)
                .build()

        )
    }
    val thirst_filter get() :Bitmap {
        return Pixelate.fromBitmap(
            bitmap,
            PixelateLayer.Builder(PixelateLayer.Shape.Square)
                .setResolution(8f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                .setResolution(18f)
                .setOffset(12f)
                .setAlpha(0.5f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                .setResolution(28f)
                .setOffset(36f)
                .setAlpha(0.5f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(16f)
                .setSize(8f)
                .setOffset(4f)
                .build()
        )
    }

    val four_filter get() :Bitmap {
        return Pixelate.fromBitmap(
            bitmap,
            PixelateLayer.Builder(PixelateLayer.Shape.Square)
                .setResolution(48f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                .setResolution(12f)
                .setSize(8f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                .setResolution(12f)
                .setSize(8f)
                .setOffset(6f)
                .build(),
                    PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(32f)
                .setSize(8f)
                .setOffset(6f)
                .build()
        )
    }

    val five_filter get() :Bitmap {
        return Pixelate.fromBitmap(
            bitmap,
            PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                .setResolution(34f)
                .setSize(25f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                .setResolution(24f)
                .setOffset(12f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Square)
                .setResolution(24f)
                .setAlpha(.8f)
                .build()
        )
    }
    val six_filter get() :Bitmap {
        return Pixelate.fromBitmap(
            bitmap,
            PixelateLayer.Builder(PixelateLayer.Shape.Square)
                .setResolution(32f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(32f)
                .setOffset(16f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(32f)
                .setOffset(0f)
                .setAlpha(0.5f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(16f)
                .setSize(9f)
                .setOffset(0f)
                .setAlpha(0.5f)
                .build()
        )
    }
    val sevent_filter get() :Bitmap {
        return Pixelate.fromBitmap(
          bitmap,
            PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(24f)
                .build(),
            PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                .setResolution(24f)
                .setSize(9f)
                .setOffset(12f)
                .build()
        )
    }
}