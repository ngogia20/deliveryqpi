import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alicom.mns.tools.DefaultAlicomMessagePuller;
import com.alicom.mns.tools.MessageListener;
import com.aliyun.mns.model.Message;
import com.google.gson.Gson;
/**
 * This is the DEMO for SMS Delivery Report API
 */
public class SMSDReport {
    //private static Log logger=LogFactory.getLog(ReceiveDemo.class);
    static class MyMessageListener implements MessageListener{
        private Gson gson=new Gson();
        @Override
        public boolean dealMessage(Message message) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("message receiver time from mns:" + format.format(new Date()));
            System.out.println("message handle: " + message.getReceiptHandle());
            System.out.println("message body: " + message.getMessageBodyAsString());
            System.out.println("message id: " + message.getMessageId());
            System.out.println("message dequeue count:" + message.getDequeueCount());
            System.out.println("Thread:" + Thread.currentThread().getName());
            try{
                Map<String,Object> contentMap=gson.fromJson(message.getMessageBodyAsString(), HashMap.class);
                String arg = (String) contentMap.get("arg");
                //Please start your own code here
            }catch(com.google.gson.JsonSyntaxException e){
                //logger.error("error_json_format:"+message.getMessageBodyAsString(),e);
                // Message will be deleted for the format error
                return true;
            } catch (Throwable e) {
                // The exception caused by your own code. Message will not be deleted and be pushed again
                return false;
            }
            // Message will be deleted for the format error
            return true;
        }
    }
    public static void main(String[] args) throws Exception, ParseException {
        DefaultAlicomMessagePuller puller=new DefaultAlicomMessagePuller();
        //Set the size of asynchronous thread
        puller.setConsumeMinThreadSize(6);
        puller.setConsumeMaxThreadSize(16);
        puller.setThreadQueueSize(200);
        puller.setPullMsgThreadSize(1);
        //Only enabled when debugging, should be disabled at working status
        puller.openDebugLog(false);
        String regionIdForPop = "ap-southeast-1";
        String endpointNameForPop = "ap-southeast-1";
        String domainForPop = "dybaseapi.ap-southeast-1.aliyuncs.com";
        String mnsAccountEndpoint = "http://1493622401794734.mns.ap-southeast-1.aliyuncs.com";
        //AccessKey, you can get if from Alicloud Console
        String accessKeyId="";
        String accessKeySecret="";




        //Message Type: SmsReport
        String messageType="SmsReport";
        //Message Quenue Name, you can get it from SMS console, like:Alicom-Queue-xxxxxx-SmsReport
        String queueName="Alicom-Queue-5185079258585835-SmsReport";
        puller.startReceiveMsgForVPC(accessKeyId, accessKeySecret, messageType, queueName, regionIdForPop,
                endpointNameForPop, domainForPop, mnsAccountEndpoint,new MyMessageListener());

        System.out.println("Queue Started");
    }
}