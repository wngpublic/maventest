package org.wayne.io;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class IOTest {
    @Before
    public void setup() {

    }

    @Test
    public void testDataOutputStream() throws IOException {
        {
            String s = "hello, i am a human";
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.write(s.getBytes());
            dataOutputStream.flush();
            byte [] byteArray = byteArrayOutputStream.toByteArray();
            dataOutputStream.close();
            byteArrayOutputStream.close();
            String s1 = new String(byteArray);
            assertTrue(!s.equals("cat in the hat"));
            assertTrue(s.equals("hello, i am a human"));
            assertTrue(s.equals(s1));
        }
        {
            String s0 = "hello, i am a human";
            String s1 = "hello, i am a cat";
            String scon = "hello, i am a human. hello, i am a cat";
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.write(s0.getBytes());
            dataOutputStream.write(". ".getBytes());
            dataOutputStream.write(s1.getBytes());
            dataOutputStream.flush();
            byte [] byteArray = byteArrayOutputStream.toByteArray();
            dataOutputStream.close();
            byteArrayOutputStream.close();
            String s2 = new String(byteArray);
            assertTrue(s2 != null);
            assertTrue(!s2.equals("cat in the hat"));
            assertTrue(s2.equals("hello, i am a human. hello, i am a cat"));
            assertTrue(s2.equals(scon));
            byteArray = s2.getBytes();
            int size = byteArray.length;
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
            byte [] byteArray2 = new byte[dataInputStream.available()];
            dataInputStream.read(byteArray2);
            String s3 = new String(byteArray2);
            assertTrue(scon.equals(s3));

        }
        {

        }
    }

}
