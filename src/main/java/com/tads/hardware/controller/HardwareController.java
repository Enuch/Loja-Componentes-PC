package com.tads.hardware.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tads.hardware.model.Hardware;
import com.tads.hardware.service.FileStorageService;
import com.tads.hardware.service.HardwareService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class HardwareController {

    HardwareService service;
    FileStorageService storageService;

    @Autowired
    public void setFileStorageService(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    public void setService(HardwareService service) {
        this.service = service;
    }

    // ROTAS ADMIN
    
    @RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
    public String getHome() {

        return "home";
    }

    @RequestMapping(value = {"/admin"}, method = RequestMethod.GET)
    public String getAdmin(Model model) {
        var listaHardware = service.findAll();
        List<Hardware> lista = new ArrayList<Hardware>();

        for (var i = 0; i < listaHardware.size(); i++) {
            if (listaHardware.get(i).getDeleted() == null) {
                lista.add(listaHardware.get(i));
            } else {

            }
        }

        model.addAttribute("listaHardware", lista);

        return "admin";
    }

    @RequestMapping(value = {"/cadastro"}, method = RequestMethod.GET)
    public String getFormCadastro(Model model) {
        Hardware hardware = new Hardware();

        model.addAttribute("hardware", hardware);

        return "cadastro";
    }

    @RequestMapping(value = {"/salvar"}, method = RequestMethod.POST)
    public String getCadastrar(@ModelAttribute @Validated Hardware hardware, Errors errors, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return "cadastro";
        } else {

            hardware.setImgUri(file.getOriginalFilename());
            service.save(hardware);
            storageService.save(file);

            redirectAttributes.addAttribute("msg", "Item salvo com sucesso!");

            return "redirect:/admin";
        }
        
    }

    @RequestMapping(value = {"/editar/{id}"})
    public ModelAndView getFormEditar(@PathVariable(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView("edita");
        Hardware hardware = service.findById(id);
        modelAndView.addObject("hardware", hardware);

        return modelAndView;
    }

    @RequestMapping(value = {"/deletar/{id}"})
    public String doDelete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        
        var deletado = service.findById(id);
        
        deletado.setDeleted(new Date());
        service.save(deletado);
        redirectAttributes.addAttribute("msg", "Item deletado com sucesso");


        return "redirect:/admin";
    }

    // ROTAS USUARIO

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public String getIndex(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Configurando sessão

        HttpSession session = request.getSession();
        // Pegando horario da sessão
        var out = response.getWriter();
        var dataCriacao = new Date(session.getCreationTime());
        var dataUltimoAcesso = new Date(session.getLastAccessedTime());
        var formataData = new SimpleDateFormat("dd/MM/yyyy");
        var dataFormatada = formataData.format(dataCriacao);

        Integer contador = (Integer)session.getAttribute("contador");
        if (contador == null) {
            contador = 0;
        } else {
            contador++;
        }

        ArrayList<Hardware> carrinho = (ArrayList<Hardware>) request.getSession().getAttribute("carrinho");
        Integer compras;
        if (carrinho == null) {
            compras = 0;
        } else {
            compras = carrinho.size();
        }

        session.setAttribute("contador", contador);

        out.println("ID: " + session.getId() +
                    " Data criação: " + formataData.format(dataCriacao) +
                    " Ultimo Acesso: " + formataData.format(dataUltimoAcesso) +
                    " Contador: " + contador +
                    " Itens no carrinho: " + compras);

        // Cookies

        Cookie acesso = new Cookie("acesso", dataFormatada);
        acesso.setMaxAge((60*24)*24);
        response.addCookie(acesso);


        // Lista de hardwares

        var listaHardware = service.findAll();
        List<Hardware> lista = new ArrayList<Hardware>();

        for (var i = 0; i < listaHardware.size(); i++) {
            if (listaHardware.get(i).getDeleted() == null) {
                lista.add(listaHardware.get(i));
            } else {

            }
        }

        model.addAttribute("listaHardware", lista);

        return "index";
    }

    @RequestMapping(value = {"/carrinho"}, method=RequestMethod.GET)
    public String getPageCarrinho(RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
    
        ArrayList<Hardware> carrinho = (ArrayList<Hardware>) request.getSession().getAttribute("carrinho");
        if (carrinho == null) {
            redirectAttributes.addAttribute("msg", "Carrinho vazio");
            return "redirect:/index";
        }
        model.addAttribute("carrinho", carrinho);
        return "/carrinho";

    }

    @RequestMapping(value = {"/addCarrinho/{id}"}, method=RequestMethod.GET)
    public String doAddCarrinho(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        Hardware hardware = service.findById(id);
        System.out.println(hardware);

        if (request.getSession().getAttribute("carrinho") == null) {
            ArrayList<Hardware> carrinho = new ArrayList<Hardware>();
            carrinho.add(hardware);

            request.getSession().setAttribute("carrinho", carrinho);
        } else {
            ArrayList<Hardware> carrinho = (ArrayList<Hardware>)request.getSession().getAttribute("carrinho");
            carrinho.add(hardware);
            System.out.println(hardware);
            request.getSession().setAttribute("carrinho", carrinho);

        }

        return "redirect:/index";
    }

    @RequestMapping(value = {"/finalizarCarrinho"}, method=RequestMethod.GET)
    public String finalizaCarrinho(HttpServletRequest request, HttpServletResponse response) {
        var session = request.getSession();
        session.invalidate();

        return "redirect:/index";
    }

}
