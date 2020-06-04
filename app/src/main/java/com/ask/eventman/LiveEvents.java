package com.ask.eventman;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;


public class LiveEvents extends Fragment {
    private RecyclerView liveEvents;
    private LiveEventsAdapter liveEventsAdapter;
    private ArrayList<HashMap<String,Object>> eventslist=new ArrayList<>();
    private DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("events");
    private LinearLayoutManager mLayoutManager;

    private int limit=7,evt_count=0,loaded=0,k;

    boolean isLoading=Boolean.FALSE,isLoaded=Boolean.FALSE,userScrolled=false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_events,container,false);
        liveEvents = (RecyclerView) view.findViewById(R.id.live_events_list);
        liveEventsAdapter = new LiveEventsAdapter(eventslist,getActivity().getApplicationContext());

        mLayoutManager = new LinearLayoutManager(getContext());
        liveEvents.setLayoutManager(mLayoutManager);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        loadLiveEvents();
        liveEvents.setAdapter(liveEventsAdapter);


        liveEvents.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                if(userScrolled && (visibleItemCount + pastVisibleItems)==totalItemCount){
                    userScrolled=false;
                    loadLiveEvents();
                }

            }
        });

    }


    protected void loadLiveEvents() {
        isLoading=Boolean.TRUE;

        eventsRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                evt_count= (int) dataSnapshot.getChildrenCount();
                if(evt_count<dataSnapshot.getChildrenCount()){
                    isLoaded=Boolean.FALSE;
                }
                if(loaded==dataSnapshot.getChildrenCount()){
                    isLoaded=Boolean.TRUE;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*if(loaded>=evt_count && loaded>0) {
            isLoaded = Boolean.TRUE;
            isLoading = Boolean.FALSE;
            return;
        }*/

        if(eventslist.size()==0){
            k=0;
            eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<HashMap<String, Object>> value = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        if(k==limit)
                            break;
                        HashMap<String,Object> item=data.getValue(value);
                        if(item.get("Owner").toString().contains(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            eventslist.add(item);
                            k+=1;
                        }
                        loaded+=1;
                    }
                    liveEventsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            Query query = eventsRef.startAt(eventslist.get(eventslist.size()-1).get("key").toString());
            eventslist.remove(eventslist.size()-1);
            loaded-=1;


            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<HashMap<String, Object>> value = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    int k=0;
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        if(k==limit)
                            break;
                        HashMap<String,Object> item=data.getValue(value);
                        if(item.get("Owner").toString()== FirebaseAuth.getInstance().getCurrentUser().getEmail()){
                            eventslist.add(item);
                            k+=1;
                        }
                        loaded++;
                    }
                    liveEventsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        isLoading=Boolean.FALSE;
    }

    public class LiveEventsAdapter extends RecyclerView.Adapter<EventsViewHolder>{

        ArrayList<HashMap<String, Object>> data;
        Context context;
        
        public LiveEventsAdapter(ArrayList<HashMap<String, Object>> arr,Context context) {
            data = arr;
            this.context=context;
        }

        @NonNull
        @Override
        public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_element,parent,false);

            return new EventsViewHolder(view);

        }



        @Override
        public void onBindViewHolder(@NonNull final EventsViewHolder holder,int position) {
            holder.event_dp.setImageDrawable(getActivity().getDrawable(R.drawable.bg_img_2));
            FirebaseStorage.getInstance().getReference().child("events/"+data.get((int)position).get("Title").toString()+"/icon.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(LiveEvents.this)
                            .load(uri)
                            .placeholder(R.drawable.app_icon)
                            .into(holder.event_dp);
                }
            });
            try{
                holder.event_title.setText(data.get((int)position).get("Title").toString());
                holder.location.setText(data.get((int)position).get("Type").toString());
                holder.date.setText(data.get((int)position).get("Start Date").toString().substring(0,2));
                holder.mthyr.setText(data.get((int)position).get("Start Date").toString().substring(3,5)+" "+data.get((int)position).get("Start Date").toString().substring(6,10));
            }
            catch(Exception e)
            {
                Toast.makeText(context, "unable to set data to the listItem", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


    private class EventsViewHolder extends RecyclerView.ViewHolder{

        ImageView event_dp;
        TextView event_title;
        TextView location;
        TextView date;
        TextView mthyr;
        LinearLayout container;
        
        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);
            event_dp = (ImageView) itemView.findViewById(R.id.event_dp);
            event_title = (TextView) itemView.findViewById(R.id.event_title);
            location = (TextView) itemView.findViewById(R.id.location);
            date = (TextView) itemView.findViewById(R.id.date);
            mthyr = (TextView) itemView.findViewById(R.id.mthyr);
            container = itemView.findViewById(R.id.elmnt_container);
        }


    }
}
