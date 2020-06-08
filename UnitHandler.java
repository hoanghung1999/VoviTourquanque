package api;

import api.response.HandlerUtil;
import com.google.gson.Gson;
import mySQL.task.SqlContentTask;
import mySQL.task.SqlUnitTask;
import mySQL.task.SqlUnitTour;
import oop.Unit;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/unit")
public class UnitHandler {
            @GET
            @Path("/get")
            @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
            public Response getUnit(@DefaultValue("")@QueryParam("name") String name){
                JSONObject data= new JSONObject();
                String code="get unit by name";
                String dataname="unit";
                if (name.equals("")) return HandlerUtil.responseDataSuccess(data,false,code);

                SqlUnitTask sqlUnitTask= new SqlUnitTask();
                List<Unit> list= sqlUnitTask.getUnitByName(name);
                Gson json= new Gson();
                String list_unit=json.toJson(list);
                data.put(dataname,list_unit);
                return HandlerUtil.responseDataSuccess(data,true,code);
            }
            // lay toan bo unit trong 1 tournament
            @GET
            @Path("/getall/{tour_id}")
            @Produces(MediaType.APPLICATION_JSON)
            public Response getAllUnit(@DefaultValue("0")@PathParam("tour_id") String tour_id){


                JSONObject data= new JSONObject();
                String code="get unit in tour by tour id";
                String dataname="list unit";
                if (tour_id.equals(""))    return HandlerUtil.responseDataSuccess(data,false,"get unit in tour by tour id");

                try {
                    int id_tour=Integer.parseInt(tour_id);
                    SqlUnitTask sqlUnitTask= new SqlUnitTask();
                    List<Unit> list= sqlUnitTask.getUnitByTourID(id_tour);
                    Gson json= new Gson();
                    String list_unit=json.toJson(list);
                    data.put(dataname,list_unit);
                    return HandlerUtil.responseDataSuccess(data,true,code);
                }catch (Exception e){
                    return HandlerUtil.responseDataSuccess(data,false,code);
                }

            }
    @POST
    @Path("/insert")
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertUnit(@DefaultValue("") @QueryParam("unit_name") String name){
                JSONObject data= new JSONObject();
                String code="insert unit";
                String dataname="unit id";
        if(name.equals(""))
            return HandlerUtil.responseDataSuccess(data,false,code);
        try {
            Unit unit= new Unit(name);
            SqlUnitTask sqlUnitTask= new SqlUnitTask();
            //todo: không để param mặc định
            int idunit=sqlUnitTask.insertUnit(unit);
            boolean status=false;
            if (idunit>0 ){
                data.put(dataname,idunit);
                status=true;
            }else {
                data.put(dataname,idunit);
            }
            data.put(dataname,idunit);
            return HandlerUtil.responseDataSuccess(data,status,code);
        }catch (Exception e){
            return HandlerUtil.responseDataSuccess(data,false,code);

        }



    }
//            cài đặt bảng  unit_tour => các đơn vị tham gia trong 1 tournament
            @POST
            //DOTO: xem lại logic phần này
            @Path("/insertCT")
            @Produces(MediaType.APPLICATION_JSON)
            public Response insertUnitTour(@DefaultValue("0")@QueryParam("list_idunit") String listidunit,
                                @DefaultValue("0") @QueryParam("tour_id") String tourid){
                JSONObject data=new JSONObject();
                String code="set up content in tour";
                String dataname="setupCTT";
                if (listidunit.equals("0")||tourid.equals("0")) {

                    return HandlerUtil.responseDataSuccess(data, false, code);

                }
                String temp[]=listidunit.split(",");
                try {
                    int idtour=Integer.parseInt(tourid);
                    List<Integer> listidCT=new ArrayList<>();
                    for(String i:temp) listidCT.add(Integer.parseInt(i));
                    SqlUnitTour sqlUnitTour= new SqlUnitTour();
                    int k=sqlUnitTour.insertUnitInTour(listidCT,idtour);
                    boolean status=false;
                    if (k == 1) {
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

            @PUT
            @Path("/update")
            @Produces(MediaType.APPLICATION_JSON)
            public  Response updateUnit(@DefaultValue("0") @QueryParam("unit_id") String unit_id,
                                      @DefaultValue("") @QueryParam("unit_name") String unit_name){
                JSONObject data= new JSONObject();
                String code="update unit by id";
                String dataname="update unit";
                if (unit_id.equals("0")|| unit_name.equals(""))
                    return HandlerUtil.responseDataSuccess(data,false,code);
                try {
                    int idunit=Integer.parseInt(unit_id);
                    Unit unit=new Unit(idunit,unit_name);
                    SqlUnitTask sqlUnitTask=new SqlUnitTask();
                    int k=sqlUnitTask.updateUnit(unit);
                    boolean status=false;// bien kiem tra qua trinh update
                    if (k == 1) {
                        data.put(dataname, "ok");
                        status=true;
                    } else {
                        data.put(dataname , "fail");
                    }
                    return HandlerUtil.responseDataSuccess(data,status,code);

                }catch (Exception e){
                    return HandlerUtil.responseDataSuccess(data,false,code);
                }
            }

            @DELETE
            @Path("/delete/{id}")
            @Produces(MediaType.APPLICATION_JSON)
            public  Response deleteUnitbyId(@DefaultValue("0")@PathParam("id") String id){
                JSONObject data= new JSONObject();
                String code="delete unit by id";
                String dataname="delete unit";
                if (id.equals("0"))
                    //todo: tạo param chung cho code
                    return HandlerUtil.responseDataSuccess(data,false,code);
                try {
                    int idunit=Integer.parseInt(id);
                    SqlUnitTask sqlUnitTask=new SqlUnitTask();
                    int k=sqlUnitTask.deleteUnitByID(idunit);
                    boolean status=false;
                    if (k == 1) {
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
