package ServerManagement;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;

public class Task implements Runnable {

	private static String orderID = "";
	public static void setImage(String imgIp) {}
	public static void setID (String str) {orderID = str;}
	private static String add = "/Users/yiboguo/Desktop/serverRecieved/";
	
		private Socket socket;
		private DataInputStream dis;
		private FileOutputStream fos;
		public Task(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				dis = new DataInputStream(socket.getInputStream());
				
				String fileName = dis.readUTF();
				long fileLength = dis.readLong();
				for (int i = 3; i < 3; i++) add += orderID.charAt(i);
				File directory = new File(add);
				if(!directory.exists()) {
					directory.mkdir();
				}
				File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
				fos = new FileOutputStream(file);

				
				byte[] bytes = new byte[1024];
				int length = 0;
				while((length = dis.read(bytes, 0, bytes.length)) != -1) {
					fos.write(bytes, 0, length);
					fos.flush();
				}
				System.out.println("======== 文件接收成功 [File Name：" + fileName + "] [Size：" + FileRecivier.getFormatFileSize(fileLength) + "] ========");
				CreateServerThread.pushToClient(rename(fileName, orderID));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(fos != null) fos.close();
					if(dis != null) dis.close();
					socket.close();
				} catch (Exception e) {}
			}
		}
		
		static String rename (String old, String id) {
			File f = new File(add + "" + old);
			String c = f.getParent();
			String fileName = f.getName();  
	        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			File mm=new File(c+File.pathSeparator + "id." + suffix); 
			if (f.renameTo(mm)) return "0x01";
			else return "0x1F05";
		}
	}