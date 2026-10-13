package otus.gpb.homework.viewandresources

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        applyContentInsets()
        findViewById<MaterialToolbar>(R.id.toolbar).apply {
            setNavigationOnClickListener { finish() }
            setOnMenuItemClickListener {
                finish()
                true
            }
        }
        listOf(R.id.cart_item_1, R.id.cart_item_2, R.id.cart_item_3, R.id.cart_item_4).forEachIndexed { index, id ->
            findViewById<View>(id).findViewById<TextView>(R.id.item_title).text =
                getString(R.string.list_item, index + 1)
        }
    }
}
