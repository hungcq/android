package raijin.firebasedatabasetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private List<UserInfo> userInfoList;
    private UserListRecyclerViewAdapter adapter;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserListActivity.this));

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserInfo, FirebaseViewHolder>(UserInfo.class, R.layout.row_user_list, FirebaseViewHolder.class, databaseReference) {

            @Override
            protected void populateViewHolder(FirebaseViewHolder viewHolder, UserInfo userInfo, int position) {
                viewHolder.name.setText(userInfo.getName());
                viewHolder.phoneNumber.setText(userInfo.getPhoneNumber());
                if (userInfo.getAvatarUrl() != null && !userInfo.getAvatarUrl().equals(""))
                    Picasso.with(UserListActivity.this).load(userInfo.getAvatarUrl()).resize(200, 200).centerCrop().into(viewHolder.avatar);
                else viewHolder.avatar.setImageDrawable(null);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
//        userInfoList = new ArrayList<>();
//        adapter = new UserListRecyclerViewAdapter(userInfoList);
//        recyclerView.setAdapter(adapter);

//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    userInfoList.add(child.getValue(UserInfo.class));
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                userInfoList.add(dataSnapshot.getValue(UserInfo.class));
//                adapter.notifyItemInserted(userInfoList.size());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }
}
