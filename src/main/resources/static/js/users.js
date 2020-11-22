
const usersVue = new Vue({
  el: '#usersDiv',
  data: {
    users: []
  },
  mounted: function() {
    const usersUrl = 'http://localhost:8080/json/users'
    fetch(usersUrl)
      .then(res => res.json())
      .then(json => {
        const users = json._embedded.users;
        usersVue.users = users
      })
  }
})

