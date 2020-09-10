package org.apperta.okramobile.cases;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apperta.okramobile.R;

import java.util.List;

public class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.ViewHolder> {

    private List<Case> mCases;

    public CasesAdapter(List<Case> cases) {
        mCases = cases;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View casesView = inflater.inflate(R.layout.item_cases, parent, false);

        return new ViewHolder(casesView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Case c = mCases.get(position);

        TextView titleTextView = holder.titleTextView;
        titleTextView.setText(c.getTitle());
        TextView dateTextView = holder.dateTextView;
        dateTextView.setText("Date Submitted: " + c.getDate());
        TextView priorityTextView = holder.priorityTextView;
        priorityTextView.setText("Priority: " + c.getPriority());
    }

    @Override
    public int getItemCount() {
        return mCases.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView dateTextView;
        public TextView priorityTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.case_title);
            dateTextView = (TextView) itemView.findViewById(R.id.case_date);
            priorityTextView = (TextView) itemView.findViewById(R.id.case_priority);

            itemView.setOnClickListener(v -> itemOnClick());
        }

        public void itemOnClick() {
            int pos = getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {
                Case clickedCase = mCases.get(pos);
                Intent intent = new Intent(itemView.getContext(), DisplayCaseActivity.class);
                String caseId = clickedCase.getId();
                intent.putExtra("case_id", caseId);
                itemView.getContext().startActivity(intent);
            }
        }
    }
}
