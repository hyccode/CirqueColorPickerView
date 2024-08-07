package com.example.myapplication;

import static java.sql.DriverManager.println;

import android.os.Bundle;
import android.util.Log;
import android.util.Printer;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentFirstBinding;
import com.example.myapplication.view.CirqueColorPickerView;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        CirqueColorPickerView picker_view = view.findViewById(R.id.picker_view);
        picker_view.setChangeListener(new CirqueColorPickerView.OnProgressChangeListener() {
            @Override
            public void onProgressChange(int duration, int progress) {

            }

            @Override
            public void onProgressChangeEnd(int duration, int progress) {
                picker_view.setVisibility(View.GONE);
            }

        });
        ImageView shouzhi = view.findViewById(R.id.shouzhi);
        binding.touchRedirectLayout.setTargetView(picker_view);
        binding.touchRedirectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击了", Toast.LENGTH_SHORT);
            }
        });

//        binding.touchRedirectLayout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                shouzhi.setVisibility(View.VISIBLE);
//                View.DragShadowBuilder builder = new View.DragShadowBuilder(v);
//                shouzhi. startDragAndDrop(null, builder, null, 0);//第三个参数是传入一个关于这个view信息的任意对象（getLocalState），它即你需要在拖拽监听中的调用event.getLocalState()获取到这个对象来操作用的(比如传入一个RecyclerView中的position)。如果不需要这个对象，传null
//                return true;
//            }
//        });

//        shouzhi.setOnDragListener((v, event) -> {
//            Log.d("containerLayout", "event.getAction():" + event.getAction());
//            switch (event.getAction()) {
//                case DragEvent.ACTION_DRAG_ENDED: //拖拽停止
//                    //your operation
//                    break;
//                case DragEvent.ACTION_DROP://拖拽中
//                    //your operation
//                    break;
//                    case DragEvent.ACTION_DRAG_STARTED:
//                    //your operation
//                    break;
//                default:
//                    break;
//            }
//            return true;
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}