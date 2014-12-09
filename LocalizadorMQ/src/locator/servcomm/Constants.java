package locator.servcomm;

/**
 * Created by rafaelfonte on 12/5/14.
 */
public class Constants {
    public static final int SRV_PORT = 1234;
    public static final String SRV_IP = "192.168.43.232";


    public static final String ctSignUpMsg = "SIGNUP";
    public static final String ctAuthMsg = "AUTH";
    public static final String ctHelloMsg = "HELLO";
    public static final String ctErrorMsg = "ERROR";
    public static final String ctOkMsg = "OK";
    public static final String ctGetMsg = "GET";
    public static final String ctSetMsg = "SET";
    public static final String ctMakeEventMsg = "MAKE_EVENT";
    public static final String ctSubsEventMsg = "SUBSCRIBE_EVENT";
    public static final String ctUnsubsEventMsg = "UNSUBSCRIBE_EVENT";
    public static final String ctUpdateEventMsg = "UPDATE_EVENT";
    public static final String ctGetRoomsMsg = "GET_ROOMS";
    public static final String ctGetUsersMsg = "GET_USERS";



    public static final int ctSignUpAct      =  0;
    public static final int ctAuthAct        =  1;
    public static final int ctGetAct         =  2;
    public static final int ctMakeEventAct   =  3;
    public static final int ctSubsEventAct   =  4;
    public static final int ctUnsubsEventAct =  5;
    public static final int ctUpdateEventAct =  6;
    public static final int ctGetRoomsAct     =  7;
    public static final int ctGetUsersAct     =  8;

    public static boolean contains(int val, int[] v){
        for(int i = 0; i < v.length; i++){
            if(v[i] == val)
                return true;
        }
        return false;
    }

    public static int [] cvrtSubscriptions(String subs){
        String[] items = subs.replaceAll("\\{", "").replaceAll("\\}", "").split(",");

        int[] results = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                results[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {};
        }
        return results;
    }

}
