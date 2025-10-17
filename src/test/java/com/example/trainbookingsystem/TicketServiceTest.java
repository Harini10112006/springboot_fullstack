package com.example.trainbookingsystem;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.trainbookingsystem.entity.Ticket;
import com.example.trainbookingsystem.repository.TicketRepository;
import com.example.trainbookingsystem.service.TicketService;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setId((int) 1L);
        ticket.setUser("Kirithi");
        ticket.setTrain("Chennai Express");
        ticket.setBookingDate(2025-10-15);
        ticket.setFinalPrice(630);
    }

    @Test
    void getAllTickets_ShouldReturnListOfTickets() {
        List<Ticket> tickets = Arrays.asList(ticket);
        when(ticketRepository.findAll()).thenReturn(tickets);

        List<Ticket> result = ticketService.getAllTickets();

        assertEquals(1, result.size());
        assertEquals(ticket.getId(), result.get(0).getId());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void getTicketById_WhenUserExists_ShouldReturnTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Optional<Ticket> result = ticketService.getTicketById(1L);

        assertTrue(result.isPresent());
        assertEquals(ticket.getBookingDate(), result.get().getBookingDate());
        assertEquals(ticket.getFinalPrice(), result.get().getFinalPrice());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void getTicketById_WhenTicketDoesNotExist_ShouldReturnEmpty() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Ticket> result = ticketService.getTicketById(1L);

        assertFalse(result.isPresent());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void createTicket_ShouldSaveAndReturnTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals(ticket.getId(), result.getId());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void updateTicket_WhenTicketExists_ShouldUpdateAndReturnTicket() {
        Ticket updatedTicket = new Ticket();
        updatedTicket.setId(3);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket result = ticketService.updateTicket(1L, updatedTicket);

        assertEquals(updatedTicket.getId(), result.getId());
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void updateTicket_WhenTicketDoesNotExist_ShouldThrowException() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> ticketService.updateTicket(1L, ticket));
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void deleteTicket_ShouldDeleteTicket() {
        doNothing().when(ticketRepository).deleteById(1L);

        ticketService.deleteTicket(1L);

        verify(ticketRepository, times(1)).deleteById(1L);
    }
}