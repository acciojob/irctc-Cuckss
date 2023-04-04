package com.driver.services;


import com.driver.EntryDto.BookTicketEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.PassengerRepository;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    PassengerRepository passengerRepository;


    public Integer bookTicket(BookTicketEntryDto bookTicketEntryDto) throws Exception {

        //Check for validity
        //Use bookedTickets List from the TrainRepository to get bookings done against that train
        // Incase the there are insufficient tickets
        // throw new Exception("Less tickets are available");
        //otherwise book the ticket, calculate the price and other details
        //Save the information in corresponding DB Tables
        //Fare System : Check problem statement
        //Incase the train doesn't pass through the requested stations
        //throw new Exception("Invalid stations");
        //Save the bookedTickets in the train Object
        //Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
        //And the end return the ticketId that has come from db
//        Train train = trainRepository.findById(bookTicketEntryDto.getTrainId()).get();
//        String route = train.getRoute();
//        String[] routeArr = route.split(",");
//        boolean departureStationOnRoute = Arrays.stream(routeArr).anyMatch(thisRoute -> thisRoute.equals(bookTicketEntryDto.getFromStation().name()));
//        boolean arrivalStationOnRoute = Arrays.stream(routeArr).anyMatch(thisRoute -> thisRoute.equals(bookTicketEntryDto.getToStation().name()));
//        if (!departureStationOnRoute || !arrivalStationOnRoute) {
//            throw new Exception("Invalid stations");
//        }
//        //otherwise book the ticket, calculate the price and other details
//        //Save the information in corresponding DBs and tables
//        int indexOfFromStation = Arrays.asList(routeArr).indexOf(bookTicketEntryDto.getFromStation().name());
//        int indexOfToStation = Arrays.asList(routeArr).indexOf(bookTicketEntryDto.getToStation().name());
//        int totalStationsInBWGivenStations = indexOfToStation - indexOfFromStation;
//
//        List<Passenger> passengerList = new ArrayList<>();
//        for (int passengerId : bookTicketEntryDto.getPassengerIds()) {
//            Passenger passenger = passengerRepository.findById(passengerId).get();
//            passengerList.add(passenger);
//        }
//
//        Ticket ticket = new Ticket();
//        ticket.setTotalFare(300 * totalStationsInBWGivenStations);
//        ticket.setPassengersList(passengerList);
//        ticket.setFromStation(bookTicketEntryDto.getFromStation());
//        ticket.setToStation(bookTicketEntryDto.getToStation());
//        ticket.setTrain(train);
//
//
//        Passenger passenger = passengerRepository.findById(bookTicketEntryDto.getBookingPersonId()).get();
//        passenger.getBookedTickets().add(ticket);
//        passengerRepository.save(passenger);
//
//        Ticket updatedTicket = ticketRepository.save(ticket);
//
//        train.getBookedTickets().add(updatedTicket);
//        trainRepository.save(train);
//
//
//        return updatedTicket.getTicketId();


        Train train=trainRepository.findById(bookTicketEntryDto.getTrainId()).get();
        List<Ticket>tickets=train.getBookedTickets();
        String route=train.getRoute();
        String routeArr []=route.split(",");

        boolean isDepartueStationOnRoute=false;
        for(String station:routeArr){
            if(bookTicketEntryDto.getFromStation().name().equalsIgnoreCase(station)){

                isDepartueStationOnRoute=true;
            }
        }

        boolean isArrivalStationOnRoute=false;
        for(String station:routeArr){
            if(bookTicketEntryDto.getToStation().name().equalsIgnoreCase(station)){

                isArrivalStationOnRoute=true;
            }
        }
        if(!isArrivalStationOnRoute||!isDepartueStationOnRoute){
            throw new Exception("InvalidStations");
        }


        int count=0;
        for(Ticket ticket:tickets){
            count+=ticket.getPassengersList().size();
        }
        int  availableSeats=train.getNoOfSeats()-count;
        if(availableSeats<bookTicketEntryDto.getNoOfSeats()){
            throw new Exception("Less tickets are available");
        }

//for(Stringstation:routeArr){
//if(bookTicketEntryDto.getFromStation().name().equalsIgnoreCase(station))
//}


        int indexFrom=Arrays.asList(routeArr).indexOf(bookTicketEntryDto.getFromStation().name());
        int indexTo=Arrays.asList(routeArr).indexOf(bookTicketEntryDto.getToStation().name());
        int differenceBetweenStations=indexTo-indexFrom;

        List<Passenger>passengerList=new ArrayList<>();
        for(int passengerId:bookTicketEntryDto.getPassengerIds()){
            Passenger passenger=passengerRepository.findById(passengerId).get();
            passengerList.add(passenger);
        }
        Ticket ticket=new Ticket();
        ticket.setTotalFare(300*differenceBetweenStations);
        ticket.setFromStation(bookTicketEntryDto.getFromStation());
        ticket.setToStation(bookTicketEntryDto.getToStation());
        ticket.setPassengersList(passengerList);
        ticket.setTrain(train);

        Passenger passenger=passengerRepository.findById(bookTicketEntryDto.getBookingPersonId()).get();
        passenger.getBookedTickets().add(ticket);
        passengerRepository.save(passenger);

        Ticket updatedTickets=ticketRepository.save(ticket);
        train.getBookedTickets().add(updatedTickets);
        trainRepository.save(train);
        return updatedTickets.getTicketId();


    }
    }
