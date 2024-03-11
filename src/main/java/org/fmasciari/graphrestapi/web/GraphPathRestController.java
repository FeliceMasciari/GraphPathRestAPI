package org.fmasciari.graphrestapi.web;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.fmasciari.graphrestapi.dto.GraphPathSearchRequest;
import org.fmasciari.graphrestapi.service.GraphPathRestServiceImpl;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/GraphPathRest")
public class GraphPathRestController {

    @Autowired
    GraphPathRestServiceImpl uNtService;

    @Value("classpath:data.json")
    Resource resourceFile;

    @GetMapping("/test")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getTest () throws ParseException, IOException {
        String result = "errore";


        if(resourceFile != null) {
            //result = resourceFile.getDescription();
            result = new String(Files.readAllBytes(resourceFile.getFile().toPath()));
        }

        if(result == null || result.equals("")) {
            File resource = new ClassPathResource("data.json").getFile();
            result = new String(Files.readAllBytes(resource.toPath()));
        }

        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
    /*
        @GetMapping("/all")
        public ResponseEntity<Object> getAll () throws ParseException {
            //List<Employee> footballers = employeeService.getAllEmployee();

            JSONParser js = new JSONParser();
            return new ResponseEntity<Object>(js.parse(uNtService.getAll()), HttpStatus.OK);
        }


        @GetMapping("/shortestPath2")
        public ResponseEntity<Object> shortestPath2 () throws ParseException {
                //List<Employee> footballers = employeeService.getAllEmployee();
           // dbr:The_Black_Panther_\(1977_film\) dbr:Stan_Lee
                JSONParser js = new JSONParser();
                return new ResponseEntity<Object>(js.parse(uNtService.shortestPath("The_Black_Panther_\\(1977_film\\)","Stan_Lee")), HttpStatus.OK);
         }

        @GetMapping("/shortestPath/{src}/{dst}")
        @ResponseBody
        public ResponseEntity<Object> shortestPath (@PathVariable String src, @PathVariable String dst) throws ParseException {
            //List<Employee> footballers = employeeService.getAllEmployee();
            // dbr:The_Black_Panther_\(1977_film\) dbr:Stan_Lee
            JSONParser js = new JSONParser();
            //return new ResponseEntity<Object>(js.parse(uNtService.shortestPath(src,dst)), HttpStatus.OK);
            return new ResponseEntity<Object>(uNtService.shortestPath(src,dst), HttpStatus.OK);
        }

    */
    @PostMapping(
            value ="/shortestPath",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ApiOperation(value = "Shortest path between source node and destination node", notes = "shortest path between source node and destination node")
    public ResponseEntity<Object> shortestPath (@RequestBody GraphPathSearchRequest request) throws ParseException {
        //List<Employee> footballers = employeeService.getAllEmployee();
        // dbr:The_Black_Panther_\(1977_film\) dbr:Stan_Lee
        JSONParser js = new JSONParser();
        //return new ResponseEntity<Object>(js.parse(uNtService.shortestPath(src,dst)), HttpStatus.OK);
        return new ResponseEntity<Object>(uNtService.shortestPath(request.getSrcNode(),request.getDstNode()), HttpStatus.OK);
    }


    @PostMapping(
            value ="/allPaths",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ApiOperation(value = "All resources and their respective paths that can reach node", notes = "All paths")
    public ResponseEntity<Object> allPaths (@Valid @RequestBody GraphPathSearchRequest request){
//@ApiParam(name = "GraphPathSearchRequest", value = "GraphPathSearchRequest -> DstNode", required =  true)
        return new ResponseEntity<Object>(uNtService.allPaths(request.getDstNode()), HttpStatus.OK);
    }
   /* @PostMapping(
            value ="/shortestDistance",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ApiOperation(value = "Distance of the shortest path between two resources")
    public ResponseEntity<Object> shortestDistance (@ApiParam(name = "GraphPathSearchRequest", value = "GraphPathSearchRequest -> SrcNode and DstNode", required =  true) @RequestBody GraphPathSearchRequest request){

        return new ResponseEntity<Object>(uNtService.allPaths(request.getDstNode()), HttpStatus.OK);
    }
*/
    @PostMapping(
            value ="/cyclicPath",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ApiOperation(value = "Cyclic path search")
    public ResponseEntity<Object> cyclicPath ( @ApiParam(name = "GraphPathSearchRequest", value = "GraphPathSearchRequest -> DstNode", required =  true) @RequestBody GraphPathSearchRequest request){

        return new ResponseEntity<Object>(uNtService.cyclicPath(request.getDstNode()), HttpStatus.OK);
    }

    @PostMapping(
            value ="/bidirectionalSearch",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ApiOperation(value = "Shortest bidirectional path between source node and destination node")
    public ResponseEntity<Object> bidirectionalSearch ( @ApiParam(name = "GraphPathSearchRequest", value = "GraphPathSearchRequest -> SrcNode and DstNode", required =  true) @RequestBody GraphPathSearchRequest request){

        return new ResponseEntity<Object>(uNtService.bidirectionalSearch(request.getSrcNode(), request.getDstNode()), HttpStatus.OK);
    }

}
