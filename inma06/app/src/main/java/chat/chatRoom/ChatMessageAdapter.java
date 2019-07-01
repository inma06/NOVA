package chat.chatRoom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnova.nova.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.himanshusoni.chatmessageview.ChatMessageView;

/**
 * Created by himanshusoni on 06/09/15.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageHolder> {
    private final String TAG = "ChatMessageAdapter";
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1;

    private List<ChatMessage> mMessages;
    private Context mContext;

    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        mContext = context;
        mMessages = data;
    }

    @Override
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage item = mMessages.get(position);
        // 내가 보낸 메시지인지 상대방이 보낸 메시지인지 확인하는 과정
        if (item.isMine()) return MY_MESSAGE;
        else return OTHER_MESSAGE;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MY_MESSAGE) {
            // 내가 보낸 메시지 ( Right 오른쪽에 보인다. )
            return new MessageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mine_message, parent, false));
        } else {
            // 상대방이 보낸 메시지 ( Left 왼쪽에 보인다. )
            return new MessageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_other_message, parent, false));
        }
    }

    // 메시지가 추가되면 mMessages List 에 메시지를 담고, 리사이클러뷰 하단에 아이템(메시지)를 추가한다.
    public void add(ChatMessage message) {
        mMessages.add(message);
        notifyItemInserted(mMessages.size() - 1);
    }

    @Override
    public void onBindViewHolder(final MessageHolder holder, final int position) {
        ChatMessage chatMessage = mMessages.get(position);
        if (chatMessage.isImage()) {
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.tvMessage.setVisibility(View.GONE);
            //TODO 이미지가 삽입되는 부분 ( 이미지 매개변수로 받기 )
//            holder.ivImage.set;
        } else if(chatMessage.isImage() == false) {
            // 메시지 ( 이미지가 아닌 )
            holder.ivImage.setVisibility(View.GONE);
            holder.tvMessage.setVisibility(View.VISIBLE);
            holder.tvMessage.setText(chatMessage.getContentMessage());
        }

        // TODO : 서버에서 UTC Time 값을 받아와서 기기의 맞는 위치(TimeZone)으로 변환하기(Converting).
        String date = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());
        holder.tvTime.setText(date);

        holder.chatMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, holder.getAdapterPosition() + " _클릭!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        ImageView ivImage;
        ChatMessageView chatMessageView;

        MessageHolder(View itemView) {
            super(itemView);
            chatMessageView = (ChatMessageView) itemView.findViewById(R.id.chatMessageView);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}