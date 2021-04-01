import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

public class AgentMain {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        String id = args[0];
        String jarName = args[1];

        System.out.println("id ==> " + id);
        System.out.println("jarName ==> " + jarName);

        VirtualMachine virtualMachine = VirtualMachine.attach(id);
        virtualMachine.loadAgent(jarName);
        virtualMachine.detach();

        System.out.println("ends");
    }
}
