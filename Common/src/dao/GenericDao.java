package dao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.RequestContext;

public interface GenericDao<Entity, PK> extends Remote {
	List<Entity> findAll() throws RemoteException;

	Entity findById(PK id) throws RemoteException;

	List<Entity> findByCritere(String critere, String valeur) throws RemoteException;

	Entity create(Entity entity, RequestContext context) throws RemoteException;

	Entity update(Entity entity, RequestContext context) throws RemoteException;

	boolean remove(PK id, RequestContext context) throws RemoteException;
}
