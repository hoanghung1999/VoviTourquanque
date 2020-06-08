package api;

import api.response.HandlerUtil;
import com.google.gson.Gson;
import mySQL.task.SqlTournamentTask;
import oop.Tournament;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("/tournament")
public class TournamentHandler {
        // API tim tournament bang ten
        @GET
        @Path("/get")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getTournament(@DefaultValue("")@QueryParam("name")String name){
            JSONObject data= new JSONObject();
            String code="get tournament by name";
            boolean status=false;
            if(!name.equals("")) {
                SqlTournamentTask sqlTournamentTask = new SqlTournamentTask();
                List<JSONObject> list = sqlTournamentTask.getTourbyName(name);
                // neu khong co ban ghi nao thi tra ve fasle
                if(list.size()==0)
                    return HandlerUtil.responseDataSuccess(data,false,code);

                data.put("tournament",list);
                status=true;
            }else {
                HandlerUtil.responseDataSuccess(data,false,code);
            }
            return HandlerUtil.responseDataSuccess(data,status,code);
        }

        //API lay toan bo tour co trong DB
        @GET
        @Path("/getall")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getAllTour(){
            JSONObject data= new JSONObject();
            String code="get all tour in DB";
            boolean status=false;
            try {
                SqlTournamentTask sqlTournamentTask= new SqlTournamentTask();
                List<JSONObject> list_tour=sqlTournamentTask.getAllTour();

                data.put("tournament",list_tour);
                status=true;

            }catch (Exception e){
                HandlerUtil.responseDataSuccess(data,false,code);
            }
            return HandlerUtil.responseDataSuccess(data,false,code);
        }

        @POST
        @Path("/insert")
        @Produces(MediaType.APPLICATION_JSON)
        // them 1 tournament va tra ve PK cua tour
        public Response insertTournament(@DefaultValue("") @QueryParam("tour_name") String tour_name,
                                       @DefaultValue("")@QueryParam("tour_sdate") String tourSDate,
                                        @DefaultValue("") @QueryParam("tour_edate") String tourEDate,
                                        @DefaultValue("") @QueryParam("tour_location") String tourLocation,
                                       @DefaultValue("") @QueryParam("tour_description") String tourDescription
    ) {
            JSONObject data=new JSONObject();
            String code="insert tournament";

            boolean status=false;
            if(tour_name.equals("")||tourSDate.equals("")||tourEDate.equals("")||tourLocation.equals("")){
                return HandlerUtil.responseDataSuccess(data,false,code);
            }

            SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false); // set false để kiểm tra tính hợp lệ của date. VD: tháng 2 phải có 28-29 ngày, năm có 12 tháng,....
            try {
                Date dateS=df.parse(tourSDate);
                Date dateE=df.parse(tourEDate);
                Tournament tournament= new Tournament(tour_name,new java.sql.Date(dateS.getTime()),new java.sql.Date(dateE.getTime()),
                        tourLocation,tourDescription);
                SqlTournamentTask sqlTournamentTask= new SqlTournamentTask();
                int  tour_id=sqlTournamentTask.insertTour(tournament);
                String dataname="tour_ID";
                if (tour_id != 0) {
                    data.put(dataname, tour_id);
                    status=true;
                } else {
                    data.put(dataname,"fail");
                }
                return HandlerUtil.responseDataSuccess(data,status,code);

            }catch (Exception e){
                return HandlerUtil.responseDataSuccess(data,false,code);
            }
        }

        @PUT
        @Path("/update")
        @Produces(MediaType.APPLICATION_JSON)
        public Response putTournament(@DefaultValue("0") @QueryParam("tour_id") String tour_id,
                                      @DefaultValue("") @QueryParam("tour_name") String tour_name,
                                      @DefaultValue("")@QueryParam("tour_sdate") String tourSDate,
                                      @DefaultValue("") @QueryParam("tour_edate") String tourEDate,
                                      @DefaultValue("") @QueryParam("tour_location") String tourLocation,
                                      @DefaultValue("") @QueryParam("tour_description") String tourDescription){
            JSONObject data=new JSONObject();
            String code="update tournament";
            boolean status=false;
            // kiem tra du lieu dau vao
            if(tour_id.equals("0")||tour_name.equals("")||tourSDate.equals("")||tourEDate.equals("")
                    ||tourLocation.equals("")){
                return HandlerUtil.responseDataSuccess(data,false,code);
            }

            SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false); // set false để kiểm tra tính hợp lệ của date. VD: tháng 2 phải có 28-29 ngày, năm có 12 tháng,....
            try {
                Date dateS=df.parse(tourSDate);
                Date dateE=df.parse(tourEDate);
                // lay id tour truyen vao
                int ID_tour=Integer.parseInt(tour_id);
                Tournament tournament= new Tournament(ID_tour,tour_name,new java.sql.Date(dateS.getTime()),new java.sql.Date(dateE.getTime()),
                        tourLocation,tourDescription);
                SqlTournamentTask sqlTournamentTask= new SqlTournamentTask();
                int  k=sqlTournamentTask.updateTour(tournament);

                // bien kiem tra qua trinh update tour
                String dataname="update tour";
                if (k > 0) {
                    data.put(dataname, "ok");
                    status=true;
                } else {
                    data.put(dataname, "fail");
                }
                return HandlerUtil.responseDataSuccess(data,status,code);

            }catch (Exception e){
                return HandlerUtil.responseDataSuccess(data,false,code);
            }
        }

        @DELETE
        @Path("/delete/{id}")
        @Produces(MediaType.APPLICATION_JSON)
        public Response deleteTournamentById(@DefaultValue("0")@PathParam("id") String id){
            JSONObject data=new JSONObject();
            String code="delete tour by id";
            boolean status=false;
            if(id.equals("0")) return HandlerUtil.responseDataSuccess(data,false,code);
            try {
                int id_tour=Integer.parseInt(id);
                SqlTournamentTask sqlTournamentTask = new SqlTournamentTask();
                int k = sqlTournamentTask.deleteTourByID(id_tour);
                //bien kiem tra qua trinh xoa tour
                String dataname="delete tour";
                if (k > 0) {
                    data.put(dataname, "ok");
                    status=true;
                } else {
                    data.put(dataname, "fail");
                }
                return HandlerUtil.responseDataSuccess(data,status,code);
            }catch (Exception e){
                return HandlerUtil.responseDataSuccess(data,false,code);
            }
        }

}
