package code.UDP;


public class UDPTest {
    EchoClient client;

    //@Before
    public void setup(){
        new EchoServer().start();
        client = new EchoClient();
    }

    //@Test
    //public static void main(String[] args) 
    //{
    //    //String echo = client.sendEcho("hello server");
    //    //System.out.println(echo);
    //    //assertEquals("hello server", echo);
    //    //echo = client.sendEcho("server is working");
    //    //System.out.println(echo);
    //    //assertFalse(echo.equals("hello server"));
    //}

    //@After
    public void tearDown() {
        client.sendEcho("end");
        client.close();
    }
}