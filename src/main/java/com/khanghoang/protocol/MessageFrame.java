package com.khanghoang.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MessageFrame {
        public String type;
        public String from;
        public String senderName;
        public String to;
        public String roomId;
        public long timestamp;
        public String content;

        public MessageFrame() {}

        public MessageFrame(String type, String from, String senderName, String to, String roomId, long timestamp, String content) {
                this.type = type;
                this.from = from;
                this.senderName = senderName;
                this.to = to;
                this.roomId = roomId;
                this.timestamp = timestamp;
                this.content = content;
        }

        public void setSenderName(String senderName) { this.senderName = senderName; }
        public String getSenderName() { return senderName; }
        public String getType()     { return type; }
        public String getFrom()     { return from; }
        public void setFrom(String from) { this.from = from; }
        public String getTo()       { return to; }
        public String getRoomId()   { return roomId; }
        public void setRoomId(String roomId) { this.roomId = roomId; }
        public long getTimestamp()  { return timestamp; }
        public String getContent()  { return content; }
        public void setContent(String content) { this.content = content; }

        // ====== Encode method ======
        public byte[] encode() {
                byte[] typeBytes = getBytes(type);
                byte[] fromBytes = getBytes(from);
                byte[] senderNameBytes = getBytes(senderName);
                byte[] toBytes = getBytes(to);
                byte[] roomBytes = getBytes(roomId);
                byte[] contentBytes = getBytes(content);

                int totalSize = Integer.BYTES + typeBytes.length +
                        Integer.BYTES + fromBytes.length +
                        Integer.BYTES + senderNameBytes.length +
                        Integer.BYTES + toBytes.length +
                        Integer.BYTES + roomBytes.length +
                        Long.BYTES +
                        Integer.BYTES + contentBytes.length;

                ByteBuffer buffer = ByteBuffer.allocate(totalSize);

                putString(buffer, typeBytes);
                putString(buffer, fromBytes);
                putString(buffer, senderNameBytes);
                putString(buffer, toBytes);
                putString(buffer, roomBytes);

                buffer.putLong(timestamp);

                putString(buffer, contentBytes);

                return buffer.array();
        }

        // ====== Decode static method ======
        public static MessageFrame decode(byte[] raw) {
                ByteBuffer buffer = ByteBuffer.wrap(raw);

                String type = getString(buffer);
                String from = getString(buffer);
                String senderName = getString(buffer);
                String to = getString(buffer);
                String roomId = getString(buffer);
                long timestamp = buffer.getLong();
                String content = getString(buffer);

                return new MessageFrame(type, from, senderName, to, roomId, timestamp, content);
        }

        // ====== Helpers ======
        private static byte[] getBytes(String str) {
                return str != null ? str.getBytes(StandardCharsets.UTF_8) : new byte[0];
        }

        private static void putString(ByteBuffer buffer, byte[] bytes) {
                buffer.putInt(bytes.length);
                if (bytes.length > 0) {
                        buffer.put(bytes);
                }
        }

        private static String getString(ByteBuffer buffer) {
                int length = buffer.getInt();
                if (length == 0) return "";
                byte[] bytes = new byte[length];
                buffer.get(bytes);
                return new String(bytes, StandardCharsets.UTF_8);
        }
}
