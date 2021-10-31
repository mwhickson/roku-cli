package x.hickson;

import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    static String RokuIpAddress = "";

    static final int MSG_KEY_HOME = 1;
    static final int MSG_KEY_UP = 2;
    static final int MSG_KEY_DOWN = 3;
    static final int MSG_KEY_LEFT = 4;
    static final int MSG_KEY_RIGHT = 5;
    static final int MSG_KEY_SELECT = 6;
    static final int MSG_KEY_BACK = 7;
    static final int MSG_KEY_INFO = 8;
    static final int MSG_KEY_POWER_OFF = 9;
    static final int MSG_KEY_VOLUME_DOWN = 10;
    static final int MSG_KEY_VOLUME_UP = 11;
    static final int MSG_KEY_VOLUME_MUTE = 12;
    static final int MSG_KEY_PLAY = 13;
    static final int MSG_KEY_REV = 14;
    static final int MSG_KEY_FWD = 15;
    static final int MSG_KEY_INSTANT_REPLAY = 16;
    static final int MSG_KEY_SEARCH = 17;
    static final int MSG_KEY_ENTER = 18;
    static final int MSG_KEY_BACKSPACE = 19;

    public static String FindRoku() throws Exception {
        String rokuAddress = "";
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        String ssdpMessage = "M-SEARCH * HTTP/1.1\r\n" +
                "Host: 239.255.255.250:1900\r\n" +
                "Man: \"ssdp:discover\"\r\n" +
                "ST: roku:ecp\r\n" +
                "\r\n";

        sendData = ssdpMessage.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("239.255.255.250"), 1900);

        DatagramSocket clientSocket = new DatagramSocket();
        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String response = new String(receivePacket.getData());

        String[] lines = response.split("\r\n");

        if (lines.length > 0) {
            if (lines[0].equals("HTTP/1.1 200 OK")) {
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].startsWith("LOCATION:")) {
                        rokuAddress = lines[i].split(" ")[1];
                    }
                }
            }
        }

        clientSocket.close();

        return rokuAddress;
    }

    public static void SendRokuMessage(String message) throws Exception {
        String dest = RokuIpAddress + message;

        String body = "POST " + message + " HTTP/1.1\r\n" +
                "Host: " + RokuIpAddress + "\r\n" +
                "\r\n";

        URL url = new URL(dest);
        URLConnection c = url.openConnection();

        HttpURLConnection http = (HttpURLConnection) c;
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "text/plain");
        http.setRequestProperty("Content-Length", body.valueOf(body.length()));

        OutputStream os = c.getOutputStream();
        os.write(body.getBytes(StandardCharsets.UTF_8));

        String response = http.getResponseMessage();

        if (!response.equals("OK")) {
            System.out.println("ERROR: " + response);
        }

        http.disconnect();
    }

    public static String getMessage(int messageCode) {
        String message = "";

        switch (messageCode) {
            case MSG_KEY_HOME:
                message = "/keypress/home";
                break;

            case MSG_KEY_UP:
                message = "/keypress/up";
                break;

            case MSG_KEY_DOWN:
                message = "/keypress/down";
                break;

            case MSG_KEY_LEFT:
                message = "/keypress/left";
                break;

            case MSG_KEY_RIGHT:
                message = "/keypress/right";
                break;

            case MSG_KEY_SELECT:
                message = "/keypress/select";
                break;

            case MSG_KEY_BACK:
                message = "/keypress/back";
                break;

            case MSG_KEY_INFO:
                message = "/keypress/info";
                break;

            case MSG_KEY_POWER_OFF:
                message = "/keypress/poweroff";
                break;

            case MSG_KEY_VOLUME_DOWN:
                message = "/keypress/volumedown";
                break;

            case MSG_KEY_VOLUME_UP:
                message = "/keypress/volumeup";
                break;

            case MSG_KEY_VOLUME_MUTE:
                message = "/keypress/volumemute";
                break;

            case MSG_KEY_PLAY:
                message = "/keypress/play";
                break;

            case MSG_KEY_REV:
                message = "/keypress/rev";
                break;

            case MSG_KEY_FWD:
                message = "/keypress/fwd";
                break;

            case MSG_KEY_INSTANT_REPLAY:
                message = "/keypress/instantreplay";
                break;

            case MSG_KEY_SEARCH:
                message = "/keypress/search";
                break;

            case MSG_KEY_ENTER:
                message = "/keypress/enter";
                break;

            case MSG_KEY_BACKSPACE:
                message = "/keypress/backspace";
                break;

            default:
                break;
        }

        return message;
    }

    public static void main(String[] args) throws Exception {
        RokuIpAddress = FindRoku();

        if (!RokuIpAddress.equals("")) {

            Scanner s = new Scanner(System.in);
            String command = "";

            System.out.println("Found Roku device at: " + RokuIpAddress);
            String lineFormat = "%10s %10s %10s %10s %10s %n";

            while (!command.toLowerCase().startsWith("q")) {

                System.out.println("");

                System.out.format(lineFormat, "[H]ome", "[U]p", "[D]own", "[L]eft", "[R]ight");
                System.out.format(lineFormat, "[S]elect", "[B]ack", "[I]nfo", "Po[w]er", "");
                System.out.format(lineFormat, "[P]lay", "Re[v]", "[F]wd", "", "");
                System.out.format(lineFormat, "Vol[+]", "Vol[-]", "[M]ute", "", "");
                System.out.format(lineFormat, "[Q]uit", "", "", "", "");

                System.out.println("");

                System.out.print("Command: ");

                command = s.nextLine().toLowerCase().substring(0, 1);

                switch(command) {
                    case "h":
                        SendRokuMessage(getMessage(MSG_KEY_HOME));
                        break;

                    case "u":
                        SendRokuMessage(getMessage(MSG_KEY_UP));
                        break;

                    case "d":
                        SendRokuMessage(getMessage(MSG_KEY_DOWN));
                        break;

                    case "l":
                        SendRokuMessage(getMessage(MSG_KEY_LEFT));
                        break;

                    case "r":
                        SendRokuMessage(getMessage(MSG_KEY_RIGHT));
                        break;

                    case "s":
                        SendRokuMessage(getMessage(MSG_KEY_SELECT));
                        break;

                    case "b":
                        SendRokuMessage(getMessage(MSG_KEY_BACK));
                        break;

                    case "i":
                        SendRokuMessage(getMessage(MSG_KEY_INFO));
                        break;

                    case "w":
                        SendRokuMessage(getMessage(MSG_KEY_POWER_OFF));
                        break;

                    case "p":
                        SendRokuMessage(getMessage(MSG_KEY_PLAY));
                        break;

                    case "v":
                        SendRokuMessage(getMessage(MSG_KEY_REV));
                        break;

                    case "f":
                        SendRokuMessage(getMessage(MSG_KEY_FWD));
                        break;

                    case "+":
                        SendRokuMessage(getMessage(MSG_KEY_VOLUME_UP));
                        break;

                    case "-":
                        SendRokuMessage(getMessage(MSG_KEY_VOLUME_DOWN));
                        break;

                    case "m":
                        SendRokuMessage(getMessage(MSG_KEY_VOLUME_MUTE));
                        break;

                    default:
                        // PASS: ... let it be...
                        break;
                }

            }

        }
        else {
            System.out.println("ERROR: could not find Roku device.");
        }

        System.out.println("done.");
    }
}
