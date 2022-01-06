package com.asecurity.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asecurity.domain.Medico;
import com.asecurity.domain.Perfil;
import com.asecurity.domain.PerfilTipo;
import com.asecurity.domain.Usuario;
import com.asecurity.service.MedicoService;
import com.asecurity.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired
	private MedicoService medicoService;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/novo/cadastro/usuario")
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {

		return "usuario/cadastro";

	}

	@GetMapping("/lista")
	public String listarUsuarios() {

		return "usuario/lista";
	}

	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDataTables(HttpServletRequest request) {

		return ResponseEntity.ok(usuarioService.buscarTodos(request));
	}

	@PostMapping("/cadastro/salvar")
	public String salvarUsuarios(Usuario usuario, RedirectAttributes attr) {
		List<Perfil> perfis = usuario.getPerfis();

		if (perfis.size() > 2 ||

				perfis.containsAll(Arrays.asList(new Perfil(PerfilTipo.ADMIN.getCod()), new Perfil(PerfilTipo.PACIENTE.getCod()))) ||

				perfis.containsAll(Arrays.asList(new Perfil(PerfilTipo.MEDICO.getCod()), new Perfil(PerfilTipo.PACIENTE.getCod())))) {

			attr.addFlashAttribute("falha", "Paciente não pode ser Admin e/ou Médico.");
			attr.addFlashAttribute("usuario", usuario);

		} else {
			try {
				usuarioService.salvarUsuario(usuario);
				attr.addFlashAttribute("sucesso", "Operação realizadao com sucesso.");
			} catch (DataIntegrityViolationException e) {
				attr.addFlashAttribute("falha", "E-mail já existe.");
			}
		}

		return "redirect:/u/novo/cadastro/usuario";

	}

	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView editarCredenciais(@PathVariable("id") Long id) {

		return new ModelAndView("usuario/cadastro", "usuario", usuarioService.buscaPorId(id));
	}

	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id") Long usuarioId, @PathVariable("perfis") Long[] perfisId) {

		Usuario us = usuarioService.buscaPorIdEPerfis(usuarioId, perfisId);

		if (us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&

				!us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {

			return new ModelAndView("usuario/cadastro", "usuario", us);

		} else if (us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {

			Medico medico = medicoService.buscarPorUsuarioId(usuarioId);

			return medico.hasNotId()

					?

					new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))

					:

					new ModelAndView("medico/cadastro", "medico", medico);

		} else if (us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
			ModelAndView model = new ModelAndView("error");
			model.addObject("status", "403");
			model.addObject("error", "Área restrita.");
			model.addObject("message", "Os dados de pacientes são restritos a ele.");

			return model;
		}
		return new ModelAndView("redirect:/u/lista");
	}

	@GetMapping("/editar/senha")
	public String editarSenha() {

		return "usuario/editar-senha";

	}

	@PostMapping("/confirmar/senha")
	public String editarSenha(@RequestParam("senha1") String senha1, @RequestParam("senha2") String senha2, @RequestParam("senha3") String senha3,
			@AuthenticationPrincipal User user, RedirectAttributes attr) {

		if (!senha1.equals(senha2)) {
			attr.addFlashAttribute("falha", "Senha não confere.");
			return "redirect:/u/editar/senha";
		}

		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());

		if (!UsuarioService.isSenhaCorreta(senha3, usuario.getSenha())) {
			attr.addFlashAttribute("falha", "Senha atual não confere.");
			return "redirect:/u/editar/senha";
		}

		usuarioService.alterarSenha(usuario, senha1);
		attr.addFlashAttribute("sucesso", "Senha alterada com sucesso.");
		return "redirect:/u/editar/senha";

	}

	@GetMapping("/novo/cadastro")
	public String novoCadastro(Usuario usuario) {
		return "cadastrar-se";
	}

	@GetMapping("/cadastro/realizado")
	public String cadastroRealizado() {

		return "fragments/mensagem";
	}

	@PostMapping("/cadastro/paciente/salvar")
	public String savlarCadastroPaciente(Usuario usuario, BindingResult result) throws MessagingException {

		try {
			usuarioService.salvarCadastroPaciente(usuario);
		} catch (DataIntegrityViolationException e) {
			result.reject("email", "Este e-mail já está cadastrado.");
			return "cadastrar-se";
		}

		return "redirect:/u/cadastro/realizado";
	}

	@GetMapping("/confirmacao/cadastro")
	public String respostaConfirmacaoCadastroPaciente(@RequestParam("codigo") String codigo, RedirectAttributes attr) {

		usuarioService.ativarCadastroPaciente(codigo);

		attr.addFlashAttribute("alerta", "sucesso");
		attr.addFlashAttribute("Titulo", "Cadastro ativado.");
		attr.addFlashAttribute("texto", "Seu cadastro está ativo.");
		attr.addFlashAttribute("subtexto", "Siga com seu login e senha.");

		return "redirect:/login";
	}

	@GetMapping("/p/redefinir/senha")
	public String pedidoRedefinirSenha() {

		return "usuario/pedido-recuperar-senha";
	}

	@GetMapping("/p/recuperar/senha")
	public String redefinirSenha(String email, ModelMap model) throws MessagingException {

		usuarioService.redefinicaoDeSenha(email);

		model.addAttribute("sucesso", "Em instantes você receberá um e-mail para redefinir sua senha.");
		model.addAttribute("usuario", new Usuario(email));

		return "usuario/recuperar-senha";

	}

	@PostMapping("/p/nova/senha")
	public String confirmacaoDeRedefinicaoDeSenha(Usuario usuario, ModelMap model) {
		Usuario u = usuarioService.buscarPorEmail(usuario.getEmail());

		if (!usuario.getCodigoVerificador().equals(u.getCodigoVerificador())) {
			model.addAttribute("falha", "Código verificador não confere.");
			return "usuario/recuperar-senha";
		}
		u.setCodigoVerificador(null);

		usuarioService.alterarSenha(u, usuario.getSenha());
		model.addAttribute("alerta", "Ssucesso");
		model.addAttribute("titulo", "Senha redefinica.");
		model.addAttribute("texto", "Você já pode logar no sistema.");

		return "login";

	}

}
