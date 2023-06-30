package dong.duan.photoedittor.edit_library.text

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import dong.duan.photoedittor.adapter.GenericAdapter
import dong.duan.photoedittor.databinding.ItemColorsViewBinding
import dong.duan.photoedittor.databinding.LayoutAddTextBinding
import dong.duan.photoedittor.file.Colors
import dong.duan.photoedittor.file.get_all_colorsxml

@Suppress("UNREACHABLE_CODE")
class OverlayTextFragment : DialogFragment() {
    private val binding by lazy {
        LayoutAddTextBinding.inflate(layoutInflater)
    }
    private lateinit var add_edit_text: EditText
    private lateinit var text_done_text_view: TextView
    private lateinit var input_method_manager: InputMethodManager
    private var text_color = Colors.APP_COLOR
    private var text_editor_event: TextEditorEvent? = null

    interface TextEditorEvent {
        fun onDone(input: String, color: Int)
    }

    override fun onStart() {
        super.onStart()
        var dialog = dialog
        if (dialog != null) {
            var width = ViewGroup.LayoutParams.MATCH_PARENT
            var height = ViewGroup.LayoutParams.MATCH_PARENT

            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Colors.TRANSPARENT))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        add_edit_text = binding.addTextEditText
        text_done_text_view = binding.addTextDoneTv
        binding.addTextEditText.focusable
        input_method_manager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.layoutColor.adapter = GenericAdapter(
            get_all_colorsxml(requireContext()),
            ItemColorsViewBinding::inflate
        ) { itemColorsViewBinding, colorFilter, i ->
            itemColorsViewBinding.itemColors.backgroundTintList =
                ColorStateList.valueOf(colorFilter.value)
            itemColorsViewBinding.itemColors.setOnClickListener {
                this.text_color = colorFilter.value
                this.add_edit_text.setTextColor(colorFilter.value)
            }
        }

        val argument = requireArguments()

        add_edit_text.setText(argument.getString(EXTRA_INPUT_TEXT))
        text_color = argument.getInt(EXTRA_INPUT_COLOR)
        add_edit_text.setTextColor(text_color)
        input_method_manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        text_done_text_view.setOnClickListener {
            input_method_manager.hideSoftInputFromWindow(it.windowToken, 0)
            dismiss()
            val inputtext = add_edit_text.text.toString()
            val text_event = text_editor_event
            if (inputtext.isNotEmpty() && text_event != null) {
                text_event.onDone(inputtext, text_color)
            }
        }


    }

    fun setOntextEdittorEvent(textEditorEvent: TextEditorEvent) {
        text_editor_event = textEditorEvent
    }

    companion object {
        const val EXTRA_INPUT_TEXT = "extra_input_text"
        const val EXTRA_INPUT_COLOR = "extra_input_color"

        fun show(
            activity: AppCompatActivity,
            inputtext: String = "",
            @ColorInt color: Int = Colors.WHITE
        )
                : OverlayTextFragment {
            val arg = Bundle()
            arg.putString(EXTRA_INPUT_TEXT, inputtext)
            arg.putInt(EXTRA_INPUT_COLOR, color)
            val fragent = OverlayTextFragment()
            fragent.arguments = arg
            fragent.show(activity.supportFragmentManager, TAG)
            return fragent
        }

    }

}