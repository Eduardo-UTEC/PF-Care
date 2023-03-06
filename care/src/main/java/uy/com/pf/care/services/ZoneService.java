package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.ZoneSaveException;
import uy.com.pf.care.model.documents.Zone;
import uy.com.pf.care.model.objects.NeighborhoodObject;
import uy.com.pf.care.repos.IZoneRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@Log
public class ZoneService implements IZoneService{

    @Autowired
    private IZoneRepo zoneRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    //private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public void save(Zone zone) {
        try{
            zoneRepo.save(zone);
            log.info("*** Zona guardada con exito: " + LocalDateTime.now());

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO ZONA: " + e);
            throw new ZoneSaveException(zone);
        }
    }

    @Override
    public List<Zone> findByCountry(String countryName) {
        return zoneRepo.findByCountryName(countryName);
    }

    @Override
    public Optional<Zone> findId(String id) {
        return zoneRepo.findById(id);
    }

    // En este caso devuelvo el id de zona por cada barrio en el objeto NeighborhoodObject.
    // queda por determinar si se devuelve asi o solo un String con el nombre del barrio
    @Override
    public List<NeighborhoodObject> findAllNeighborhoods(String cityName, String departmentName, String countryName) {
        //return zoneRepo.findByCityNameAndDepartmentNameAndCountryName(cityName, departmentName, countryName);
        List<NeighborhoodObject> list = new ArrayList<>();
        mongoTemplate.query(Zone.class).stream().forEach(zone ->{
                    if(zone.getCityName().equals(cityName) &&
                       zone.getDepartmentName().equals(departmentName) &&
                       zone.getCountryName().equals(countryName))

                       list.add(new NeighborhoodObject(zone.getZone_id(), zone.getNeighborhoodName()));
                });

        return list.stream().sorted(new Comparator<NeighborhoodObject>() {
            @Override
            public int compare(NeighborhoodObject o1, NeighborhoodObject o2) {
                return o1.getNeighborhoodName().compareTo(o2.getNeighborhoodName());
            }
        }).toList();
    }

    @Override
    public List<String> findAllCities(String departmentName, String countryName) {
        return mongoTemplate.query(Zone.class)
                .distinct("cityName")
                .matching(new Query(
                        where("departmentName").is(departmentName).and("countryName").is(countryName)))
                .as(String.class)
                .all()
                .stream().sorted().toList();
    }

    @Override
    public List<String> findAllDepartments(String countryName) {
        return mongoTemplate.query(Zone.class)
                .distinct("departmentName")
                .matching(new Query(
                        where("countryName").is(countryName)))
                .as(String.class)
                .all()
                .stream().sorted().toList();
    }

    @Override
    public List<String> findAllCountries() {
        return mongoTemplate.query(Zone.class)
                .distinct("countryName")
                .matching(new Query(
                        where("countryName").not().isNullValue()))
                .as(String.class)
                .all()
                .stream().sorted().toList();
    }

}
