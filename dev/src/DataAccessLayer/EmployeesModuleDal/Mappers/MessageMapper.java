package DataAccessLayer.EmployeesModuleDal.Mappers;

import BusinessLayer.EmployeeModule.Objects.JobMessages;
import DataAccessLayer.DeliveryModuleDal.DControllers.DalController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MessageMapper extends DalController {

    private Map<String, Map<String,List<String>>> messagesMapper; //branch , job , message

    public MessageMapper() {
        super("Messages");
        messagesMapper = new ConcurrentHashMap<>();
    }

    public List<JobMessages> selectAllMessages() throws Exception {
        return (List<JobMessages>)(List<?>)select();
    }

    public boolean insert (String job, String mes , String branch)
    {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?,?,?)",
                getTableName(), "BranchAddress" , "Job" , "Message");
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branch);
            pstmt.setString(2, job);
            pstmt.setString(3, mes);
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        if (!messagesMapper.containsKey(branch))
        {
            messagesMapper.put(branch,new ConcurrentHashMap<>());
            messagesMapper.get(branch).put(job,new ArrayList<>());
            messagesMapper.get(branch).get(job).add(mes);
        }
        else
        {
            if (!messagesMapper.get(branch).containsKey(job))
            {
                messagesMapper.get(branch).put(job,new ArrayList<>());
            }
            messagesMapper.get(branch).get(job).add(mes);

        }
        return true;
    }
    public void deleteMessage( String branch,String job, String mes ) {
        delete(job, mes, branch, "Job",  "Message", "BranchAddress");
        if(messagesMapper.containsKey(branch) && messagesMapper.get(branch).containsKey(job))
            messagesMapper.get(branch).get(job).remove(mes);
    }

    public void deleteMessageForJob(String job, String mes ) {
        delete(job, mes, "Job", "Message" );
        for(String branch : messagesMapper.keySet())
            if(messagesMapper.get(branch).containsKey(job))
                messagesMapper.get(branch).get(job).remove(mes);
    }

    protected JobMessages ConvertReaderToObject(ResultSet reader) throws Exception {
        JobMessages result = null;
        try {
            result = new JobMessages(reader.getString(1), reader.getString(2), reader.getString(3));
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {

    }


}
