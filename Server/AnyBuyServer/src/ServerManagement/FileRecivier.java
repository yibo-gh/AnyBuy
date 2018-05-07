package ServerManagement;
  
import java.math.RoundingMode;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.text.DecimalFormat;  

public class FileRecivier extends ServerSocket {

	private static final int SERVER_PORT = 17805;
	private static DecimalFormat df = null;
	static {
		df = new DecimalFormat("#0.0");
		df.setRoundingMode(RoundingMode.HALF_UP);
		df.setMinimumFractionDigits(1);
		df.setMaximumFractionDigits(1);
	}
	
	public FileRecivier() throws Exception {
		super(SERVER_PORT);
	}

	public void load() throws Exception {
		Socket socket = this.accept();
		new Thread(new Task(socket)).start();
	}
	
	static String getFormatFileSize(long length) {
		double size = ((double) length) / (1 << 30);
		if(size >= 1) {
			return df.format(size) + "GB";
		}
		size = ((double) length) / (1 << 20);
		if(size >= 1) {
			return df.format(size) + "MB";  
        }
		size = ((double) length) / (1 << 10);
		if(size >= 1) {
			return df.format(size) + "KB";
		}
		return length + "B";
	}
}