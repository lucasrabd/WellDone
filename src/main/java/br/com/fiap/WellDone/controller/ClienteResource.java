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

import br.com.fiap.WellDone.model.Cliente;
import br.com.fiap.WellDone.repository.ClienteRep;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping(value = "/index_cliente")
public class ClienteResource {

	

	@Autowired
	private ClienteRep clienteRepository;

	@Operation(description = "Este serviço retorna todas as clientes da base de dados", summary = "Retorna todas as clientes", tags = "Consulta")
	@GetMapping(value = "/todos")
	public List<Cliente> ConsultaTodos() {
		List<Cliente> lista = clienteRepository.findAll();
		for (Cliente i : lista) {
			i.add(linkTo(methodOn(ClienteResource.class).findById(i.getId_cliente()))
					.withRel("deseja consultar a pessoa por ID? Acesse este link: "));
		}
		return lista;
	}

	@Operation(description = "Este serviço retorna clientes por id", summary = "Retorna cliente por id", tags = "Consulta")
	@GetMapping(value = "/{id_cliente}")
	public Cliente findById(@PathVariable Long id_cliente) {

		Optional<Cliente> cliente = clienteRepository.findById(id_cliente);

		cliente.get().add(linkTo(methodOn(ClienteResource.class).ConsultaTodos())
				.withRel("deseja consultar todos clientes? Acesse este link: "));
		cliente.get().add(linkTo(methodOn(ClienteResource.class).inserirCliente(null))
				.withRel("deseja inserir um cliente? Acesse este link: "));
		cliente.get().add(linkTo(methodOn(ClienteResource.class).atualizarCliente(id_cliente, null))
				.withRel("deseja atualizar um cliente? Acesse este link: "));
		cliente.get().add(linkTo(methodOn(ClienteResource.class).apagarCliente(id_cliente))
				.withRel("deseja deletar um cliente? Acesse este link: "));

		return cliente.get();

		// return clienteRepository.findById(id_cliente).orElse(null);
	}

	@Operation(description = "Este serviço possibilita a inserção de clientes no banco de dados", summary = "Inerção de clientes", tags = "Inserção")
	@ResponseStatus(CREATED)
	@PostMapping()
	public Cliente inserirCliente(@RequestBody Cliente cliente) {
		clienteRepository.save(cliente);
		return clienteRepository.save(cliente);
	}

	@Operation(description = "Este serviço possiblita a remoção de clientes do banco de dados", summary = "Remoção de clientes", tags = "Remoção")
	@DeleteMapping(value ="/remove_cliente/{id_cliente}")
	public Cliente apagarCliente(@PathVariable Long id_cliente) {

		Cliente c1 = clienteRepository.findById(id_cliente).get();
		clienteRepository.deleteById(id_cliente);
		return c1;

	}

	@Operation(description = "Este serviço possibilita a atualziação de clientes no banco de dados", summary = "Atualização de clientes", tags = "Atualização")
	@PutMapping(value ="/atualiza_cliente/{id_cliente}")
	@Transactional
	public ResponseEntity<?> atualizarCliente(@PathVariable Long id_cliente, @RequestBody Cliente clienteAtualizado) {
		return clienteRepository.findById(id_cliente).map(cliente -> {
			
			cliente.setNome(clienteAtualizado.getNome());
			cliente.setEndereco(clienteAtualizado.getEndereco());
			cliente.setNr_telefone(clienteAtualizado.getNr_telefone());
			cliente.setEmail(clienteAtualizado.getEmail());
			cliente = clienteRepository.save(cliente);
			return ResponseEntity.ok(cliente);
		}).orElse(ResponseEntity.notFound().build());
	}

}