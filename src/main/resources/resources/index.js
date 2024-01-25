
fetch("rest/common/current-user")
.then(response => response.json())
.then(label => document.getElementById("userLabel").innerHTML = "Пользователь: "+label)

const getUsers = async () => {
      const response = await fetch("rest/admin/users");
      const users = await response.json();
      const template = document.getElementById("userTemplate");
      const container = document.getElementById("usersContainer");
      container.replaceChildren([]);
      users.forEach(user => {
        var clone = template.cloneNode(true);
        clone.id = 'trash';
        clone.hidden = false;
        clone.children[0].innerHTML = user.username;
        clone.children[1].innerHTML = user.authorities.map(obj => obj.authority).join(', ');
        clone.children[2].innerHTML = user.enabled ? 'disable' : 'enable';
        clone.children[2].onclick = () => toggleUserAvailability(user.username, !user.enabled);
        container.appendChild(clone);
      });
};

const toggleUserAvailability = async (login, state) => {
      const response = await fetch(
          `rest/admin/users/${login}/availability`,
          {
              method: "PUT",
              headers: {
                         'Accept': 'application/json',
                         'Content-Type': 'application/json'
                       },
              body: JSON.stringify(state)
          },
      );
      const resp = await response;
      getUsers();
}

const getSessions = async () => {
      const response = await fetch("rest/admin/sessions");
      const sessions = await response.json();
      const template = document.getElementById("sessionTemplate");
      const container = document.getElementById("sessionsContainer");
      container.replaceChildren([]);
      sessions.forEach(session => {
        var clone = template.cloneNode(true);
        clone.id = 'trash';
        clone.hidden = false;
        clone.children[0].innerHTML = session.sessionId;
        clone.children[1].innerHTML = session.principal.username;
        clone.children[2].innerHTML = session.expired ? "expired" : "active";
        clone.children[3].innerHTML = session.expired ? "remove" : "close";
        clone.children[3].onclick = () => deleteSession(session.sessionId);
        container.appendChild(clone);
      });
};

const deleteSession = async (sessionId) => {
      const response = await fetch(
          `rest/admin/sessions/${sessionId}`,
          { method: "DELETE" },
      );
      const resp = await response;
      getSessions();
}

const logout = () => {
    window.navigation.navigate("logout");
}