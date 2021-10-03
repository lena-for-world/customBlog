var addCategory = () => {
  var newbut = document.querySelector("button");
  var form = document.querySelector('form');
  newbut.parentNode.removeChild(newbut);
  var newli= document.createElement('p');
  var cname = '추가할&nbsp카테고리를&nbsp적어주세요';
  newli.innerHTML = "<input name='name' placeholder="+cname+">"
      + "<button className='mt-2 btn btn-white submit' type='submit'>수정</button>";
  form.appendChild(newli);
}