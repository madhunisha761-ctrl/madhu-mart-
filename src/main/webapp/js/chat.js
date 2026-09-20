function sendChat() {
  var box = document.getElementById('chatmsg');
  var log = document.getElementById('chatlog');
  var text = box.value.trim();
  if (!text) { return; }
  var me = document.createElement('div');
  me.textContent = 'You: ' + text;
  log.appendChild(me);
  box.value = '';
  fetch('chatbot', {
    method: 'POST',
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    body: 'message=' + encodeURIComponent(text)
  }).then(function (r) { return r.json(); }).then(function (d) {
    var bot = document.createElement('div');
    bot.textContent = 'Bot: ' + d.reply;
    log.appendChild(bot);
    log.scrollTop = log.scrollHeight;
  }).catch(function () {
    var err = document.createElement('div');
    err.textContent = 'Bot: Something went wrong';
    log.appendChild(err);
  });
}
