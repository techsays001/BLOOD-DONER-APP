package com.msg91.sendotp.sample.ui.send;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.msg91.sendotp.sample.R;

public class SendFragment extends Fragment {
TextView hos;
    private SendViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);

        hos=root.findViewById(R.id.bd);

        hos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent inten = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?&saddr="

                                + "&daddr= blood bank"

                        ));
                startActivity(inten);
            }
        });

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?&saddr="

                        + "&daddr= blood bank"

                ));
        startActivity(intent);
        return root;
    }
}