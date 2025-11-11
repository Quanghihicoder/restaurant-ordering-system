package com.example.qfood.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.transition.Explode
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.qfood.R
import com.example.qfood.databinding.ActivityMainBinding
import com.example.qfood.fragments.ActivityFragment
import com.example.qfood.fragments.CartFragment
import com.example.qfood.fragments.HomeFragment
import com.example.qfood.fragments.MenuFragment
import com.example.qfood.item.Food
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

            // Set an exit transition
            exitTransition = Explode()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeScreen -> replaceFragment(HomeFragment())
                R.id.menuScreen -> replaceFragment(MenuFragment())
                R.id.cartScreen -> replaceFragment(CartFragment())
                R.id.activityScreen -> replaceFragment(ActivityFragment())
                else -> {

                }
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()
    }

    // navigate fragment
    fun changeSelected(id: Int) {
        binding.bottomNavigation.selectedItemId = id
    }

    // get image from assets
    fun getImage(src: String): Drawable? {
        val imageSrc: InputStream = assets.open(src)
        val d = Drawable.createFromStream(imageSrc, null)
        return d
    }

    override fun onBackPressed() {
        // do nothing, block to back by pressing the back button
    }

    fun logout() {
        finish()
    }

    inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    // get menu items from share preferences
    fun readMenuInPref(): MutableList<Food> {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val jsonString: String? = sharedPreferences.getString("FOODS", "")
        val gson = Gson()
        val type: Type = genericType<MutableList<Food>>()
        return gson.fromJson(jsonString, type)
    }
}