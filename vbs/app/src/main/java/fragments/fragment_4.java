package fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vbs.veli_menu;
import com.example.vbs.MenuActivity;
import com.example.vbs.R;
import com.example.vbs.Register;
import com.example.vbs.getlocation;

public class fragment_4 extends Fragment {

    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_4, container, false);
        button= (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(getActivity(), veli_menu.class);
                startActivity(in);
            }
        });
        return view;

    }
    public void kullanmayabasla(){

    }
}