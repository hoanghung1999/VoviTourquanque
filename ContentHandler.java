package api;

import api.response.HandlerUtil;
import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import mySQL.task.SqlContentTask;
import oop.Content;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.annotation.Generated;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
@Path("/content")
public class ContentHandler {
    final static Logger logger=Logger.getLogger(ContentHandler.class);
    //AIT la ALL in tour
    @GET
    @Path("/getAIT/{id}")
    @Produces(MediaType.APPLICATION_JSON)
//     lay tat ca noi dung thi dau co trong tournament
    public Response getAllConetentInTour(@DefaultValue("0")@PathParam("id") String tour_id) {
        JSONObject data= new JSONObject();
        String code="all content in tour";
        if (tour_id.equals("0")){
            logger.warn("chua nhap vao tour_id");
            return HandlerUtil.responseDataSuccess(data,false,code);
        }
        try {
            int id_tour=Integer.parseInt(tour_id);
            SqlContentTask sqlContentTask=new SqlContentTask();
            List<Content> list= sqlContentTask.getAllContentByTourId(id_tour);
            Gson json=new Gson();
            String list_ct=json.toJson(list);
            data.put("content",list_ct);
            return HandlerUtil.responseDataSuccess(data,true,code);
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.warn(sw.toString());
            return HandlerUtil.responseDataSuccess(data,false,code);
        }



    }
    @GET
    @Path("/getAllcontent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContent(){
        String code="get all content in DB";
        JSONObject data=new JSONObject();
        SqlContentTask sqlContentTask= new SqlContentTask();
        List<Content> list=sqlContentTask.getAllContent();
        Gson json=new Gson();
        String all_content=json.toJson(list);

        data.put("all content",all_content);
        return HandlerUtil.responseDataSuccess(data,true ,code);
    }


    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContentById(@DefaultValue("0") @PathParam("id") int id){
            JSONObject data= new JSONObject();
            String code="get content by id";
        try {
            SqlContentTask sqlContentTask = new SqlContentTask();
            Content content = sqlContentTask.getContentByID(id);

            Gson json = new Gson();
            String json_content = json.toJson(content);
            data.put("content", json_content);
            return HandlerUtil.responseDataSuccess(data,true,code);
        }catch (Exception e){
            e.printStackTrace();
            return HandlerUtil.responseDataSuccess(  data,false,code);

        }

    }

    //    cai dat bang content_tour khi cai dat noi dung thi dau co trong tour
    //nội dung thi đấu sẽ hiển thị ra và người dùng sẽ chỉ việc chọn các nội dung có trong giải
    @POST
    @Path("/insertCT")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postContentTourByTour(@DefaultValue("") String list_idCT,@DefaultValue("0")  String tour_id){
        JSONObject data= new JSONObject();
        String code="insert content tour";
        // kiểm tra dữ liệu đầu vào đã đc nhập chưa
        if (list_idCT.equals("")||tour_id.equals("0"))
            return HandlerUtil.responseDataSuccess(data,false,code);

        try {
            String [] listID = list_idCT.split(",");
            int idtour=Integer.parseInt(tour_id);
            List<Integer> idlist= new ArrayList<>();
            try {
                for(String i:listID) idlist.add(Integer.parseInt(i));
            }catch (Exception e){
                return HandlerUtil.responseDataSuccess(data,false,code);
            }

            SqlContentTask sqlContentTask = new SqlContentTask();

            int k= sqlContentTask.insertContentTour(idlist,idtour);

            if (k == 1) {
                data.put(code, "ok");
                return HandlerUtil.responseDataSuccess(data,true,code);
            } else {
                data.put(code,"fail");
                return HandlerUtil.responseDataSuccess(data,false,code);
            }

        }catch (Exception e){
            return HandlerUtil.responseDataSuccess(data,false,code);
        }

    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTournament(@DefaultValue("0") @QueryParam("content_id") String content_id,
                                     @DefaultValue("") @QueryParam("content_name") String content_name,
                                     @DefaultValue("") @QueryParam("content_type") String content_type){
        JSONObject data= new JSONObject();
        String code="update content";
        if(content_id.equals("0")||content_name.equals("")||content_type.equals(""))
            return HandlerUtil.responseDataSuccess(data,false,code);
        try {
            int id_content=Integer.parseInt(content_id);
            Content content= new Content(id_content,content_name,content_type);
           
            SqlContentTask sqlContentTask= new SqlContentTask();
            int k=sqlContentTask.updateContent(content);

            if (k == 0) {
                data.put(code,"fail");
                return HandlerUtil.responseDataSuccess(data,false,code);
                // cap nhat content ko thanh cong
            } else {
                // cap nhat conten thanh cong
                data.put(code, "ok");
                return HandlerUtil.responseDataSuccess(data,true,"update content");

            }
        }catch (Exception e){
            return HandlerUtil.responseDataSuccess(data,false,"update content");
        }

    }



    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteContentById(@DefaultValue("0")@PathParam("id") String content_id){

        String code="delete content by id";
        JSONObject data= new JSONObject();
        if (content_id.equals("0"))
            return HandlerUtil.responseDataSuccess(data,false,code);
        try {
            int id_content=Integer.parseInt(content_id);
            SqlContentTask sqlContentTask= new SqlContentTask();
            int k=sqlContentTask.deleteContent(id_content);

            String code2="delete content";
            if (k == 0) {
                data.put(code2,"fail");
                return HandlerUtil.responseDataSuccess(data,false,code);
            } else {
                data.put(code2, "ok");
                return HandlerUtil.responseDataSuccess(data,true,"delete content");
            }

        }catch (Exception e){
            return HandlerUtil.responseDataSuccess(data,false,code);
        }

    }
}
