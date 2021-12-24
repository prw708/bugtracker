function loginSubmit(token) {
	document.getElementById("12-form").submit();
}

function createSubmit(token) {
	document.getElementById("3-form").submit();
}

function viewSubmit(token) {
	document.getElementById("22-form").submit();
}

function discussionSubmit(token) {
	document.getElementById("4-form").submit();
}

function handleSelectThread(threadIndex) {
	document.getElementById("4-form").elements['threadIndex'].value = threadIndex;
	document.getElementById("displaySelectedThread").textContent = "Replying to Thread #" + (parseInt(threadIndex, 10) + 1);
}

function handleUnselectThread() {
	document.getElementById("4-form").elements['threadIndex'].value = "-1";
	document.getElementById("displaySelectedThread").textContent = "";
}

function deleteHandler(e) {
	e.preventDefault();
    grecaptcha.ready(function() {
		var recaptchaSiteKey = document.querySelector("meta[name='recaptchaSiteKey']").getAttribute("content");
    	grecaptcha.execute(recaptchaSiteKey, {action: 'delete'}).then(function(token) {
			e.target.parentNode.elements['recaptcha'].value = token;
			e.target.parentNode.submit();
		});
	});
}

function deleteCommentHandler(e) {
	e.preventDefault();
    grecaptcha.ready(function() {
		var recaptchaSiteKey = document.querySelector("meta[name='recaptchaSiteKey']").getAttribute("content");
    	grecaptcha.execute(recaptchaSiteKey, {action: 'deleteComment'}).then(function(token) {
			e.target.parentNode.elements['recaptcha'].value = token;
			e.target.parentNode.submit();
		});
	});
}