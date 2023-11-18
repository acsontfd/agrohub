//package com.example.myapplication;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class NavigationHandler extends AppCompatActivity {
//    ActivityMainBinding binding;
//
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        replace fragment(new HomeFragment());
//
//        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
//            switch (item.getItemId()){
//                case R.id.home:
//                    replaceFragment(new home());
//                    break;
//                case R.id.profile:
//                    replaceFragment(new community());
//                    break;
//                case R.id.community:
//                    replaceFreagement(new profile());
//
//            }
//        });
//    }
//}
