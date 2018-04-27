public class generalExperiment {

	public static void main(String args[]) {
		java.sql.Timestamp ts = new java.sql.Timestamp(1524710112316L);
		System.out.println(ts);
		java.sql.Timestamp ts2 = new java.sql.Timestamp(System.currentTimeMillis());
		System.out.println(ts2);
		System.out.println(ts2.getTime() - ts.getTime());
		long l = Long.parseLong("" + 1524710112316L);
		System.out.println(l + 1);
	}
	// TODO Security Verification Time Constant = 0x927C0;
}
