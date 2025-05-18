package com.example.csempeshop;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private ArrayList<ShoppingItem> mProductList;
    private ShoppingItemAdapter mAdapter;
    private int gridNumber = 1;
    private FirebaseFirestore mFirestore;
    private CollectionReference mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop);

        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Toast.makeText(this, "Be vagy jelentkezve!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nem vagy bejelentkezve!", Toast.LENGTH_SHORT).show();
        }

        mRecyclerView = findViewById(R.id.shopRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));

        mProductList = new ArrayList<>();
        mAdapter = new ShoppingItemAdapter(this, mProductList);
        mRecyclerView.setAdapter(mAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mProducts = mFirestore.collection("Products");
        boolean inSaleListing = getIntent().getBooleanExtra("inSaleListing", false);


        queryData(inSaleListing);

        //initializeData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.csempe_shop_menubar, menu);
        return true;
    }

    private void queryData(boolean inSale) {
        mProductList.clear();

        if (!inSale) {
            mProducts.orderBy("title").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
                Log.d("Firestore", "Sikeres feltöltés.");
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ShoppingItem item = document.toObject(ShoppingItem.class);
                    item.setId(document.getId());
                    mProductList.add(item);
                }

                if (mProductList.isEmpty()) {
                    initializeData();
                    queryData(false);
                }
                mAdapter.notifyDataSetChanged();
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Hiba a feltöltés során", e);
            });
        } else {
            mProducts.whereEqualTo("inSale", true).orderBy("title").limit(100).get().addOnSuccessListener(queryDocumentSnapshots -> {
                Log.d("Firestore", "Sikeres feltöltés akciós termékekkel.");
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ShoppingItem item = document.toObject(ShoppingItem.class);
                    item.setId(document.getId());
                    mProductList.add(item);
                }

                if (mProductList.isEmpty()) {
                    initializeData();
                    queryData(false);
                }
                mAdapter.notifyDataSetChanged();
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Hiba a feltöltés során", e);
            });
        }
    }

    public void deleteProduct(ShoppingItem product) {
        DocumentReference ref = mProducts.document(product._getId());
        Log.d("ShopActivity", "DELETION:. ID: " + product._getId());

        ref.delete().addOnSuccessListener(success -> {
            Log.d("ShopActivity", "Az elem sikeresen törölve. ID: " + product._getId());
        }).addOnFailureListener(fail -> {
            Toast.makeText(this, "Az " + product._getId() + ". azonosítójú elemet nem sikerült törölni!", Toast.LENGTH_LONG).show();
        });

        queryData(false);
    }

    private void initializeData() {
        String[] productsList = getResources().getStringArray(R.array.shopping_item_titles);
        String[] productsInfo = getResources().getStringArray(R.array.shopping_item_infos);
        String[] productsPrice = getResources().getStringArray(R.array.shopping_item_prices);
        TypedArray productsImageResource = getResources().obtainTypedArray(R.array.shopping_item_images);

        //mProductList.clear();

        for ( int i = 0; i < productsList.length; i++) {
            mProducts.add(new ShoppingItem(productsList[i],
                    productsInfo[i],
                    productsPrice[i],
                    productsImageResource.getResourceId(i, 0)));
        }

        productsImageResource.recycle();

         //mAdapter.notifyDataSetChanged();
    }
}