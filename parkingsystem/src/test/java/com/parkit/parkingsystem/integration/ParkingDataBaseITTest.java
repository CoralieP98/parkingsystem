package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseITTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO =new TicketDAO();
    //private static TicketDAO ticketDAO= Mockito.mock(TicketDAO.class);

    private static DataBasePrepareService dataBasePrepareService;

    private static FareCalculatorService fareCalculatorService;

    private static Ticket ticket;

    private static ParkingService parkingService;



    @Mock
    private static InputReaderUtil inputReaderUtil= Mockito.mock(InputReaderUtil.class);

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        parkingService = new ParkingService(inputReaderUtil,parkingSpotDAO,ticketDAO);
        fareCalculatorService =new FareCalculatorService(ticketDAO);

    }

//    @BeforeEach
//    eEntries();public void setUpPerTest() throws Exception {
////       when(inputReaderUtil.readSelection()).thenReturn(1);
////        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("MEH");
////        dataBasePrepareService.clearDataBas
//
//
//
//    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();


        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        Ticket ticket = ticketDAO.getTicket("MEH");

//        assertEquals("MEH",ticket.getVehicleRegNumber());
//        int parkingNumber = ticket.getParkingSpot().getId();
////        assertEquals();


    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database
    }

    @Test
    public void CompareTicket(){

        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket= new Ticket();

        Date outTime = new Date();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("MEH");



        ticketDAO.saveTicket(ticket);
        ticketDAO.compareTicket(ticket);

        fareCalculatorService.calculateFare(ticket);


        assertEquals(ticketDAO.compareTicket(ticket), true);

    }

    @Test
    public void DoesUpdateTicketWork(){
        parkingService.processIncomingVehicle();

        Date inTime = new Date();
//        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));

//        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket= new Ticket();

//        Date outTime = new Date();
        ticket.setInTime(inTime);

        parkingService.processExitingVehicle();
//        ticket.setOutTime(outTime);
//        ticket.setParkingSpot(parkingSpot);
//        ticket.setVehicleRegNumber("MEH");

        ticketDAO.saveTicket(ticket);
//        ticketDAO.updateTicket(ticket);

//        fareCalculatorService.calculateFare(ticket);

        assertThat(ticketDAO.updateTicket(ticket)).isNotNull();

    }

//    @Test
//    public void testParkingToExit(){
//        parkingService.processIncomingVehicle();
//        parkingService.processExitingVehicle();
//
////        Ticket ticket= ticketDAO.getTicket("ABCDEF");
//        assertEquals(0,ticket.getPrice());
////        assertTrue(ticket.getOutTime(),.isNotNull());
//
//    }

}



