package com.example.mysubmission2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mysubmission2.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<UserData>(EXTRA_DATA) as UserData

        Glide.with(this)
            .load(user.avatar)
            .into(binding.civAvatarDt)
        binding.tvUsernameDt.text = user.username
        binding.tvNameDt.text = user.name
        binding.tvLocationDt.text = user.location
        binding.tvRepositoryDt.text = user.repository
        binding.tvCompanyDt.text = user.company


        val sectionPageAdapter = SectionPageAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionPageAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        supportActionBar?.elevation = 0f
    }

}