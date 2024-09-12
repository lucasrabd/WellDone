package br.com.fiap.WellDone.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.WellDone.model.Pagamento;
import br.com.fiap.WellDone.repository.PagamentoRep;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping(value = "/index_pagamento")
public class PagamentoResource {

	@Autowired
	private PagamentoRep pagamentoRepository;

	@Operation(description = "Este serviço retorna todas as pagamentos da base de dados", summary = "Retorna todas as pagamentos", tags = "Consulta")
	@GetMapping(value = "/todos")
	public List<Pagamento> ConsultaTodos() {
		List<Pagamento> lista = pagamentoRepository.findAll();
		for (Pagamento i : lista) {
			i.add(linkTo(methodOn(PagamentoResource.class).findById(i.getPagamento_id()))
					.withRel("Quer consultar o pagamento por ID? Acesse este link: "));
		}
		return lista;
		// return pagamentoRepository.findAll();
	}

	@Operation(description = "Este serviço retorna pagamentos por id", summary = "Retorna pagamento por id", tags = "Consulta")
	@GetMapping(value = "/{pagamento_id}")
	public Pagamento findById(@PathVariable Long pagamento_id) {

		Optional<Pagamento> pagamento = pagamentoRepository.findById(pagamento_id);

		pagamento.get().add(linkTo(methodOn(PagamentoResource.class).ConsultaTodos())
				.withRel("deseja consultar todos pagamentos? Acesse este link: "));
		pagamento.get().add(linkTo(methodOn(PagamentoResource.class).inserirPagamento(null))
				.withRel("deseja inserir um pagamento? Acesse este link: "));

		return pagamento.get();

		// return pagamentoRepository.findById(pagamento_id).get();
	}

	@Operation(description = "Este serviço possibilita a inserção de pagamentos no banco de dados", summary = "Inerção de pagamentos", tags = "Inserção")
	@ResponseStatus(CREATED)
	@PostMapping()
	public Pagamento inserirPagamento(@RequestBody Pagamento pagamento) {
		pagamentoRepository.save(pagamento);
		return pagamentoRepository.save(pagamento);
	}

	@Operation(description = "Este serviço possiblita a remoção de pagamentos do banco de dados", summary = "Remoção de pagamentos", tags = "Remoção")
	@DeleteMapping(value = "/remove_pagamento/{pagamento_id}")
	public Pagamento apagarPagamento(@PathVariable Long pagamento_id) {

		Pagamento p1 = pagamentoRepository.findById(pagamento_id).get();
		pagamentoRepository.deleteById(pagamento_id);
		return p1;
	}

	@Operation(description = "Este serviço possibilita a atualziação de pagamentos no banco de dados", summary = "Atualização de pagamentos", tags = "Atualização")
	@PutMapping(value = "/atualiza_pagamento/{pagamento_id}")
	@Transactional
	public ResponseEntity<?> atualizarPagamento(@PathVariable Long pagamento_id,@RequestBody Pagamento pagamentoAtualizado) {
		return pagamentoRepository.findById(pagamento_id).map(pagamento -> {
		pagamento.setPagamento_metodo(pagamentoAtualizado.getPagamento_metodo());	
		pagamento.setPagamento_vlr_total(pagamentoAtualizado.getPagamento_vlr_total());
		pagamento = pagamentoRepository.save(pagamento);
			
			return ResponseEntity.ok(pagamento);					
		}).orElseGet(()->ResponseEntity.notFound().build());
}

}
