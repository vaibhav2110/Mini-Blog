package com.vp.firebaseapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BranchMaterialFragment extends Fragment {
    public static int y=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView branchRecycler = (RecyclerView)inflater.inflate(R.layout.activity_branch_material_fragment,
                container, false);

        String[] branchNames = new String[Branch.branchs.length];
        for (int i = 0; i < branchNames.length; i++) {
            branchNames[i] = Branch.branchs[i].getName();
        }

        int[] branchImages = new int[Branch.branchs.length];
        for (int i = 0; i < branchImages.length; i++) {
            branchImages[i] = Branch.branchs[i].getImageResourceId();
        }

        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(branchNames, branchImages);
        branchRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        branchRecycler.setLayoutManager(layoutManager);
        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            public void onClick(int position) {
                y=position;
                Intent intent = new Intent(getActivity(), TermDetailActivity.class);
                intent.putExtra("hello", position);
                getActivity().startActivity(intent);
            }
        });

        return branchRecycler;
    }
}
