package my.edu.utar.ezsplit;

import static android.content.ContentValues.TAG;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DataRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static List<Ower> dataList;
    public static List<Ower> owerDataList;
    public static List<Expense> expenseDataList;
    private int layoutType;
    private double totalAmount;

    // layoutType 1: Members
    // layoutType 2: New Expense (For Whom)
    // layoutType 3: Split Equally
    // layoutType 4: Split By Ratio
    // layoutType 5: Split By Amount
    // layoutType 6: Main

    // Constructors
    public DataRecyclerViewAdapter(List<?> dataList, int layoutType) {
        this.layoutType = layoutType;

        switch (layoutType) {
            case 2:
                this.owerDataList = (List<Ower>) dataList;
                break;
            case 6:
                this.expenseDataList = (List<Expense>) dataList;
                break;
            default:
                this.dataList = (List<Ower>) dataList;
        }
    }

    public DataRecyclerViewAdapter(List<Ower> dataList, int layoutType, double totalAmount) {
        this.dataList = dataList;
        this.layoutType = layoutType;
        this.totalAmount = totalAmount;
    }

//    public DataRecyclerViewAdapter(List<Ower> dataList, int layoutType) {
//        this.dataList = dataList;
//        this.layoutType = layoutType;
//    }
//    public DataRecyclerViewAdapter(List<Ower> dataList) {
//        this.owerDataList = (List<Ower>) dataList;
//        this.layoutType = 2;
//    }
//
//    public DataRecyclerViewAdapter(List<Expense> dataList, char c) {
//        this.expenseDataList = (List<Expense>) dataList;
//        this.layoutType = 6;
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;

        // Inflate the item layout for each item
        switch (layoutType) {
            case 1:
                View viewLayout1 = inflater.inflate(R.layout.item_cardview, parent, false);
                viewHolder = new ViewHolderLayout1(viewLayout1);
                break;
            case 2:
                View viewLayout2 = inflater.inflate(R.layout.item_cardview2, parent, false);
                viewHolder = new ViewHolderLayout2(viewLayout2);
                break;
            case 3:
                View viewLayout3 = inflater.inflate(R.layout.item_cardview3, parent, false);
                viewHolder = new ViewHolderLayout3(viewLayout3);
                break;
            case 4:
                View viewLayout4 = inflater.inflate(R.layout.item_cardview4, parent, false);
                viewHolder = new ViewHolderLayout4(viewLayout4);
                break;
            case 5:
                View viewLayout5 = inflater.inflate(R.layout.item_cardview5, parent, false);
                viewHolder = new ViewHolderLayout5(viewLayout5);
                break;
            case 6:
                View viewLayout6 = inflater.inflate(R.layout.item_cardview6, parent, false);
                viewHolder = new ViewHolderLayout6(viewLayout6);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (layoutType) {
            case 1:
                Ower data1 = dataList.get(position);
                ViewHolderLayout1 viewHolderLayout1 = (ViewHolderLayout1) holder;
                viewHolderLayout1.tvFirstLetterOfMemberName.setText(String.valueOf(data1.getFirstLetterOfName()));
                viewHolderLayout1.tvMemberName.setText(data1.getName());
                break;
            case 2:
                Ower owerData = owerDataList.get(position);
                ViewHolderLayout2 viewHolderLayout2 = (ViewHolderLayout2) holder;
                viewHolderLayout2.tvFirstLetterOfMemberName.setText(String.valueOf(owerData.getFirstLetterOfName()));
                viewHolderLayout2.tvMemberName.setText(owerData.getName());
                viewHolderLayout2.tvAmount.setText(String.valueOf(MainActivity.decimalFormat.format(owerData.getAmount())));
                break;
            case 3:
                Ower data3 = dataList.get(position);
                ViewHolderLayout3 viewHolderLayout3 = (ViewHolderLayout3) holder;
                viewHolderLayout3.tvFirstLetterOfMemberName.setText(String.valueOf(data3.getFirstLetterOfName()));
                viewHolderLayout3.tvMemberName.setText(data3.getName());
                viewHolderLayout3.tvRM.setText("RM " + MainActivity.decimalFormat.format(data3.getAmount()));
                viewHolderLayout3.cb.setChecked(data3.isChecked());
                break;
            case 4:
                Ower data4 = dataList.get(position);
                ViewHolderLayout4 viewHolderLayout4 = (ViewHolderLayout4) holder;
                viewHolderLayout4.tvFirstLetterOfMemberName.setText(String.valueOf(data4.getFirstLetterOfName()));
                viewHolderLayout4.tvMemberName.setText(data4.getName());
                viewHolderLayout4.tvRM.setText("RM " + MainActivity.decimalFormat.format(data4.getAmount()));

                if (data4.getRatio() != 0)
                    viewHolderLayout4.editRatio.setText(String.valueOf(data4.getRatio()));
                else
                    viewHolderLayout4.editRatio.setHint(String.valueOf(data4.getRatio()));
                break;
            case 5:
                Ower data5 = dataList.get(position);
                ViewHolderLayout5 viewHolderLayout5 = (ViewHolderLayout5) holder;
                viewHolderLayout5.tvFirstLetterOfMemberName.setText(String.valueOf(data5.getFirstLetterOfName()));
                viewHolderLayout5.tvMemberName.setText(data5.getName());

                if (data5.getAmount() != 0)
                    viewHolderLayout5.editAmount.setText(String.valueOf(MainActivity.decimalFormat.format(data5.getAmount())));
                else
                    viewHolderLayout5.editAmount.setHint(String.valueOf(MainActivity.decimalFormat.format(data5.getAmount())));
                break;
            case 6:
                Expense expenseData = expenseDataList.get(position);
                ViewHolderLayout6 viewHolderLayout6 = (ViewHolderLayout6) holder;
                viewHolderLayout6.tvFirstLetterOfPayer.setText(String.valueOf(Character.toUpperCase(expenseData.getPayer().charAt(0))));
                viewHolderLayout6.tvPurpose.setText(expenseData.getPurpose());
                viewHolderLayout6.tvTotalAmount.setText("RM " + MainActivity.decimalFormat.format(expenseData.getTotalAmount()));
                viewHolderLayout6.tvDate.setText(expenseData.getDate());
                viewHolderLayout6.tvPayer.setText(expenseData.getPayer());
                int size = expenseData.getOwer().size();
                for (int i = 0; i < size; i++) {
                    viewHolderLayout6.tvOwer.setText(viewHolderLayout6.tvOwer.getText() + expenseData.getOwer().get(i) + " (RM " + expenseData.getAmountOwed().get(i) + ")");

                    if (i < size - 1)
                        viewHolderLayout6.tvOwer.setText(viewHolderLayout6.tvOwer.getText() + "\n");
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (layoutType) {
            case 1:
            case 3:
            case 4:
            case 5:
                return dataList.size();
            case 2:
                return owerDataList.size();
            case 6:
                return expenseDataList.size();
            default:
                return 0;
        }
    }

    // Hold views for each item in the RecyclerView
    static class ViewHolderLayout1 extends RecyclerView.ViewHolder {
        TextView tvFirstLetterOfMemberName, tvMemberName;

        ViewHolderLayout1(@NonNull View itemView) {
            super(itemView);
            tvFirstLetterOfMemberName = itemView.findViewById(R.id.tvFirstLetterOfMemberName);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
        }
    }

    class ViewHolderLayout2 extends RecyclerView.ViewHolder {
        TextView tvFirstLetterOfMemberName, tvMemberName, tvAmount;

        ViewHolderLayout2(@NonNull View itemView) {
            super(itemView);
            tvFirstLetterOfMemberName = itemView.findViewById(R.id.tvFirstLetterOfMemberName2);
            tvMemberName = itemView.findViewById(R.id.tvMemberName2);
            tvAmount = itemView.findViewById(R.id.tvAmount2);

        }
    }

    class ViewHolderLayout3 extends RecyclerView.ViewHolder {
        CardView cvOwe;
        TextView tvFirstLetterOfMemberName, tvMemberName, tvRM;
        CheckBox cb;
        double equalAmount;

        ViewHolderLayout3(@NonNull View itemView) {
            super(itemView);
            cvOwe = itemView.findViewById(R.id.cvOwe3);
            tvFirstLetterOfMemberName = itemView.findViewById(R.id.tvFirstLetterOfMemberName3);
            tvMemberName = itemView.findViewById(R.id.tvMemberName3);
            tvRM = itemView.findViewById(R.id.tvRM3);
            cb = itemView.findViewById(R.id.cb3);

            cvOwe.setBackgroundResource(R.drawable.cardview_outline);

            cvOwe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // Get the position of the clicked item in the RecyclerView

                    // Ensure the position is valid
                    if (position != RecyclerView.NO_POSITION) {
                        Ower data = dataList.get(position);
                        data.setChecked(!data.isChecked()); // Toggle checkbox state and update the data

                        // Update the checkedCount
                        if (data.isChecked())
                            Ower.incrementCheckedCount();
                        else
                            Ower.decrementCheckedCount();

                        // Calculate equal amount for each checked member
                        equalAmount = totalAmount / Ower.getCheckedCount();

                        for (int i = 0; i < dataList.size(); i++) {
                            if (dataList.get(i).isChecked())
                                dataList.get(i).setAmount(equalAmount);
                            else
                                dataList.get(i).setAmount(0);
                        }

                        notifyDataSetChanged(); // Notify the adapter of the data changes
                    }
                }
            });
        }
    }

    class ViewHolderLayout4 extends RecyclerView.ViewHolder {
        CardView cvOwe;
        TextView tvFirstLetterOfMemberName, tvMemberName, tvRM;
        EditText editRatio;

        ViewHolderLayout4(@NonNull View itemView) {
            super(itemView);
            cvOwe = itemView.findViewById(R.id.cvOwe4);
            tvFirstLetterOfMemberName = itemView.findViewById(R.id.tvFirstLetterOfMemberName4);
            tvMemberName = itemView.findViewById(R.id.tvMemberName4);
            tvRM = itemView.findViewById(R.id.tvRM4);
            editRatio = itemView.findViewById(R.id.editRatio4);

            cvOwe.setBackgroundResource(R.drawable.cardview_outline);

            cvOwe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    // Ensure the position is valid
                    if (position != RecyclerView.NO_POSITION) {
                        editRatio.requestFocus();
                    }
                }
            });

            editRatio.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int position = getAdapterPosition();
                    Log.d(TAG, "xxx: " + position);
                    // Ensure the position is valid
                    if (position != RecyclerView.NO_POSITION) {
                        Ower data = dataList.get(position);

                        int oldRatio = data.getRatio();

                        // Update the ratio property of the Ower object when the user finishes entering
                        String ratioText = editable.toString();
                        int newRatio = ratioText.isEmpty() ? 0 : Integer.parseInt(ratioText);

                        if (oldRatio != newRatio) {
                            data.setRatio(newRatio);
                            Ower.modifyTotalRatio(newRatio - oldRatio);

                            if (Ower.getTotalRatio() == 0) {
                                data.setAmount(0);
                                notifyItemChanged(position);
                            } else {
                                // Calculate amount based on the ratio for each checked member
                                for (int i = 0; i < dataList.size(); i++) {
                                    Ower ower = dataList.get(i);

                                    if (ower.getRatio() != 0 || i == position) {
                                        ower.setAmount(totalAmount * ower.getRatio() / Ower.getTotalRatio());
                                        notifyItemChanged(i);
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    class ViewHolderLayout5 extends RecyclerView.ViewHolder {
        CardView cvOwe;
        TextView tvFirstLetterOfMemberName, tvMemberName;
        EditText editAmount;

        ViewHolderLayout5(@NonNull View itemView) {
            super(itemView);
            cvOwe = itemView.findViewById(R.id.cvOwe5);
            tvFirstLetterOfMemberName = itemView.findViewById(R.id.tvFirstLetterOfMemberName5);
            tvMemberName = itemView.findViewById(R.id.tvMemberName5);
            editAmount = itemView.findViewById(R.id.editAmount5);

            cvOwe.setBackgroundResource(R.drawable.cardview_outline);

            cvOwe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        editAmount.requestFocus();
                    }
                }
            });

            editAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Update the amount property of the Ower object when the user finishes entering
                    String amountText = editable.toString();
                    double newAmount = amountText.isEmpty() ? 0 : Double.parseDouble(amountText);
                    dataList.get(getAdapterPosition()).setAmount(newAmount);
                }
            });
        }
    }

    static class ViewHolderLayout6 extends RecyclerView.ViewHolder {
        TextView tvFirstLetterOfPayer, tvPurpose, tvTotalAmount, tvDate, tvPayer, tvOwer;

        ViewHolderLayout6(@NonNull View itemView) {
            super(itemView);
            tvFirstLetterOfPayer = itemView.findViewById(R.id.tvFirstLetterOfPayer6);
            tvPurpose = itemView.findViewById(R.id.tvPurpose6);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount6);
            tvDate = itemView.findViewById(R.id.tvDate6);
            tvPayer = itemView.findViewById(R.id.tvPayer6);
            tvOwer = itemView.findViewById(R.id.tvOwer6);
        }
    }
}