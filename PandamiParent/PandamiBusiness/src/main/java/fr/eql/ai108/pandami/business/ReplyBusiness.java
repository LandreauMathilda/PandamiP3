package fr.eql.ai108.pandami.business;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import fr.eql.ai108.pandami.entity.Reply;
import fr.eql.ai108.pandami.ibusiness.ReplyIBusiness;
import fr.eql.ai108.pandami.idao.ReplyIDao;

@Remote(ReplyIBusiness.class)
@Stateless
public class ReplyBusiness implements ReplyIBusiness {
	
	@EJB
	private ReplyIDao proxyReply;

	@Override
	public List<Reply> displayOwnedReplies(Integer id) {
		return proxyReply.getAllByUser(id);
	}

	@Override
	public Reply updateReply(Reply reply) {
		return proxyReply.update(reply);
	}

	@Override
	public Reply createReply(Reply reply) {
		return proxyReply.add(reply);
	}

	@Override
	public List<Reply> displayRepliesByDemandId(Integer id) {
		return proxyReply.getAllByDemandId(id);
	}

	@Override
	public List<Reply> getNotSelectedRepliesByDemandId(Integer id) {
		return proxyReply.getAllExceptSelectedByDemandId(id);
	}

	@Override
	public List<Reply> displayPastOwnedReplies(Integer id) {
		return proxyReply.getAllPastRepliesByUser(id);
	}

	@Override
	public List<Reply> updateRejectedReplies(List<Reply> replies) {
		LocalDateTime today = LocalDateTime.now();
		List<Reply> updatedList = new ArrayList<Reply>();
		for (Reply reply : replies) {
			reply.setRejectDate(today);
			updateReply(reply);
			updatedList.add(reply);
		}
		return updatedList;
	}

	@Override
	public Reply updateSelectedReply(Reply reply) {
		LocalDateTime today = LocalDateTime.now();
		reply.setSelectionDate(today);
		return updateReply(reply);
	}

	@Override
	public String displayStatusByReply(Reply reply) {
		String status="";
		LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.now());
		if (reply.getDesistDate() != null) {
			status = "Le volontaire s'est desisté";
		} else if (reply.getSelectionDate() != null) {
			status = "Vous avez selectionné ce volontaire";
		} else if (reply.getRejectDate() != null) {
			status = "";
		}
		return status;
	}

	/*
	@Override
	public String displayReplyStatusByReply(Reply reply) {
		String status="";
		if (reply.getSelectionDate() != null && reply.getDesistDate() == null) {
			status = "volontaire selectionné";
		} 
		return status;
	}
	*/

}
